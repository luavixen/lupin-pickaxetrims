package dev.foxgirl.pickaxetrims.shared;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.BlockEvent;
import dev.architectury.event.events.common.LootEvent;
import dev.architectury.event.events.common.TickEvent;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.architectury.utils.value.IntValue;
import dev.foxgirl.pickaxetrims.shared.effect.CryingObsidianMultiBreakEffect;
import dev.foxgirl.pickaxetrims.shared.effect.LapisGlowEffect;
import dev.foxgirl.pickaxetrims.shared.effect.RedstoneVeinMineEffect;
import net.minecraft.block.BlockState;
import net.minecraft.component.ComponentType;
import net.minecraft.item.*;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.entry.EmptyEntry;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Function;

public final class PickaxeTrimsImpl implements TickEvent.Server, BlockEvent.Break, LootEvent.ModifyLootTable {

    private static PickaxeTrimsImpl INSTANCE;

    public static PickaxeTrimsImpl getInstance() {
        return INSTANCE;
    }

    public final @NotNull PickaxeTrimsConfig config;

    private final @NotNull DeferredRegister<ComponentType<?>> registryComponentType;
    private final @NotNull DeferredRegister<Item> registryItem;
    private final @NotNull DeferredRegister<ItemGroup> registryItemGroup;

    private final @NotNull RegistrySupplier<@NotNull ComponentType<PickaxeTrim>> trimComponentType;
    private final @NotNull RegistrySupplier<@NotNull SmithingTemplateItem> smithingTemplateItem;
    private final @NotNull RegistrySupplier<@NotNull ItemGroup> itemGroup;

    public @NotNull ComponentType<PickaxeTrim> getTrimComponentType() {
        return trimComponentType.get();
    }
    public @NotNull SmithingTemplateItem getSmithingTemplateItem() {
        return smithingTemplateItem.get();
    }
    public @NotNull ItemGroup getItemGroup() {
        return itemGroup.get();
    }

    public @NotNull RegistryKey<ItemGroup> getItemGroupKey() {
        return RegistryKey.of(itemGroup.getRegistryKey(), itemGroup.getId());
    }

    private <T extends Item> RegistrySupplier<T> registerItem(Identifier id, Function<Item.Settings, T> function, Item.Settings settings) {
        return registryItem.register(id, () -> function.apply(settings.registryKey(RegistryKey.of(registryItem.getRegistrar().key(), id))));
    }

    public PickaxeTrimsImpl() {
        INSTANCE = this;

        config = PickaxeTrimsConfig.loadConfig();

        registryComponentType
            = DeferredRegister.create("pickaxetrims", RegistryKeys.DATA_COMPONENT_TYPE);
        registryItem
            = DeferredRegister.create("pickaxetrims", RegistryKeys.ITEM);
        registryItemGroup
            = DeferredRegister.create("pickaxetrims", RegistryKeys.ITEM_GROUP);

        trimComponentType = registryComponentType.register(
            Identifier.of("pickaxetrims", "trim"),
            () -> ComponentType
                .<PickaxeTrim>builder()
                .codec(PickaxeTrim.CODEC)
                .packetCodec(PickaxeTrim.PACKET_CODEC)
                .cache()
                .build()
        );
        smithingTemplateItem = registerItem(
            Identifier.of("pickaxetrims", "fracture_armor_trim_smithing_template"),
            SmithingTemplateItem::of, new Item.Settings().rarity(Rarity.RARE)
        );
        itemGroup = registryItemGroup.register(
            Identifier.of("pickaxetrims", "pickaxe_trims_tab"),
            () -> CreativeTabRegistry.create(
                Text.translatable("itemGroup.pickaxetrims"),
                () -> PickaxeTrim.set(new ItemStack(Items.NETHERITE_PICKAXE), PickaxeTrim.TrimType.EMERALD)
            )
        );

        PickaxeTrimModels.forEach(id -> registerItem(id, Item::new, new Item.Settings().maxCount(0)));
    }

    private CryingObsidianMultiBreakEffect cryingObsidianEffect;
    private RedstoneVeinMineEffect redstoneEffect;
    private LapisGlowEffect lapisEffect;

    public void initialize() {
        registryComponentType.register();
        registryItem.register();
        registryItemGroup.register();

        LootEvent.MODIFY_LOOT_TABLE.register(this);
        TickEvent.SERVER_POST.register(this);
        BlockEvent.BREAK.register(this);

        cryingObsidianEffect = new CryingObsidianMultiBreakEffect();
        redstoneEffect = new RedstoneVeinMineEffect();
        lapisEffect = new LapisGlowEffect();
    }

    public void forEachStackForItemGroup(@NotNull Consumer<@NotNull ItemStack> consumer) {
        for (var pickaxeType : PickaxeTrim.PickaxeType.VALUES) {
            for (var trimType : PickaxeTrim.TrimType.VALUES) {
                consumer.accept(PickaxeTrim.set(new ItemStack(pickaxeType.getItem()), trimType));
            }
        }
        consumer.accept(new ItemStack(getSmithingTemplateItem()));
    }

    private boolean shouldModifyLootTable(RegistryKey<LootTable> key) {
        return config.smithingTemplateLootTables.contains(key.getValue().toString());
    }
    private int getLootTableEmptyEntryWeight() {
        return Math.max(0, 1 - (int) (1.0F / config.smithingTemplateLootProbability));
    }

    @Override
    public void modifyLootTable(RegistryKey<LootTable> key, LootEvent.LootTableModificationContext context, boolean builtin) {
        if (shouldModifyLootTable(key)) {
            context.addPool(
                LootPool.builder()
                    .rolls(ConstantLootNumberProvider.create(1.0F))
                    .with(EmptyEntry.builder().weight(getLootTableEmptyEntryWeight()))
                    .with(ItemEntry.builder(getSmithingTemplateItem()).weight(1))
            );
        }
    }

    @Override
    public void tick(@NotNull MinecraftServer server) {
        cryingObsidianEffect.onTickEnd(server);
        redstoneEffect.onTickEnd(server);
        lapisEffect.onTickEnd(server);
    }

    @Override
    public EventResult breakBlock(
        @NotNull World level,
        @NotNull BlockPos pos,
        @NotNull BlockState state,
        @NotNull ServerPlayerEntity player,
        @Nullable IntValue xp
    ) {
        var trimType = PickaxeTrim.getTrimType(player.getMainHandStack());
        if (trimType != null) {
            switch (trimType) {
                case CRYING_OBSIDIAN -> cryingObsidianEffect.onBlockBreak(level, pos, state, player);
                case REDSTONE -> redstoneEffect.onBlockBreak(level, pos, state, player);
                case LAPIS_LAZULI -> lapisEffect.onBlockBreak(level, pos, state, player);
                default -> {}
            }
        }
        return EventResult.pass();
    }

}
