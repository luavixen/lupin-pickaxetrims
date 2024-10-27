package dev.foxgirl.pickaxetrims.shared;

import com.google.common.collect.ImmutableList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record PickaxeTrim(@NotNull TrimType type) {

    /*
    public static final @NotNull Codec<PickaxeTrim> CODEC = RecordCodecBuilder.create(
        instance -> instance
            .group(RegistryFixedCodec.of(RegistryKeys.ITEM).fieldOf("ingredient").forGetter(PickaxeTrim::ingredient))
            .apply(instance, PickaxeTrim::new)
    );
    */

    /*
    public static final @NotNull PacketCodec<RegistryByteBuf, PickaxeTrim> PACKET_CODEC = PacketCodec.tuple(
        PacketCodecs.registryEntry(RegistryKeys.ITEM), PickaxeTrim::ingredient,
        PacketCodecs.BOOL, PickaxeTrim::showInTooltip,
        PickaxeTrim::new
    );
    */

    public static final String NBT_KEY = "pickaxetrims_trim_type";

    public enum PickaxeType {
        NETHERITE,
        DIAMOND,
        GOLD,
        IRON;

        public static final @NotNull List<PickaxeType> VALUES = ImmutableList.copyOf(values());
        public static final int COUNT = VALUES.size();

        private static @Nullable PickaxeType from(@NotNull Item item) {
            if (item == Items.NETHERITE_PICKAXE) return NETHERITE;
            if (item == Items.DIAMOND_PICKAXE) return DIAMOND;
            if (item == Items.GOLDEN_PICKAXE) return GOLD;
            if (item == Items.IRON_PICKAXE) return IRON;
            return null;
        }

        public @NotNull Item getItem() {
            return switch (this) {
                case NETHERITE -> Items.NETHERITE_PICKAXE;
                case DIAMOND -> Items.DIAMOND_PICKAXE;
                case GOLD -> Items.GOLDEN_PICKAXE;
                case IRON -> Items.IRON_PICKAXE;
            };
        }

        @Override
        public @NotNull String toString() {
            return switch (this) {
                case NETHERITE -> "netherite_pickaxe";
                case DIAMOND -> "diamond_pickaxe";
                case GOLD -> "gold_pickaxe";
                case IRON -> "iron_pickaxe";
            };
        }
    }

    public enum TrimType {
        CRYING_OBSIDIAN {
            public @NotNull String toString() { return "crying_obsidian"; }
            public @NotNull Item getItem() { return Items.CRYING_OBSIDIAN; }
            public @NotNull Formatting getColor() { return Formatting.DARK_PURPLE; }
        },
        LAPIS_LAZULI {
            public @NotNull String toString() { return "lapis_lazuli"; }
            public @NotNull Item getItem() { return Items.LAPIS_LAZULI; }
            public @NotNull Formatting getColor() { return Formatting.BLUE; }
        },
        EMERALD {
            public @NotNull String toString() { return "emerald"; }
            public @NotNull Item getItem() { return Items.EMERALD; }
            public @NotNull Formatting getColor() { return Formatting.GREEN; }
        },
        QUARTZ {
            public @NotNull String toString() { return "quartz"; }
            public @NotNull Item getItem() { return Items.QUARTZ; }
            public @NotNull Formatting getColor() { return Formatting.WHITE; }
        },
        REDSTONE {
            public @NotNull String toString() { return "redstone"; }
            public @NotNull Item getItem() { return Items.REDSTONE; }
            public @NotNull Formatting getColor() { return Formatting.RED; }
        },
        COPPER {
            public @NotNull String toString() { return "copper"; }
            public @NotNull Item getItem() { return Items.COPPER_INGOT; }
            public @NotNull Formatting getColor() { return Formatting.GOLD; }
        };

        public static final @NotNull List<TrimType> VALUES = ImmutableList.copyOf(values());
        public static final int COUNT = VALUES.size();

        private static final @NotNull Map<String, TrimType> BY_NAME = Util.make(new HashMap<>(), (map) -> {
            for (var type : VALUES) map.put(type.toString(), type);
        });

        public static @Nullable TrimType parse(@Nullable String name) {
            return BY_NAME.get(name);
        }

        public static @Nullable TrimType from(@NotNull Item item) {
            if (item == Items.CRYING_OBSIDIAN) return CRYING_OBSIDIAN;
            if (item == Items.LAPIS_LAZULI) return LAPIS_LAZULI;
            if (item == Items.EMERALD) return EMERALD;
            if (item == Items.QUARTZ) return QUARTZ;
            if (item == Items.REDSTONE) return REDSTONE;
            if (item == Items.COPPER_INGOT) return COPPER;
            return null;
        }

        public abstract @NotNull Item getItem();
        public abstract @NotNull Formatting getColor();

        public @NotNull Text getMaterialText() {
            return Text.translatable("pickaxetrims.material." + this).formatted(getColor());
        }
        public @NotNull Text getDescriptionText() {
            return Text.translatable("pickaxetrims.description." + this).formatted(getColor());
        }
    }

    public @NotNull TrimType trimType() {
        return type();
    }

    public static @Nullable PickaxeTrim get(@NotNull ItemStack stack) {
        if (stack.hasNbt()) {
            var trimType = TrimType.BY_NAME.get(stack.getNbt().getString(NBT_KEY));
            if (trimType != null) return new PickaxeTrim(trimType);
        }
        return null;
    }

    public static @NotNull ItemStack set(@NotNull ItemStack stack, @NotNull PickaxeTrim trim) {
        return set(stack, trim.trimType());
    }
    public static @NotNull ItemStack set(@NotNull ItemStack stack, @NotNull TrimType trimType) {
        stack.getOrCreateNbt().putString(NBT_KEY, trimType.toString());
        return stack;
    }

    public static @Nullable PickaxeType getPickaxeType(@Nullable ItemStack stack) {
        if (stack == null) return null;
        return PickaxeType.from(stack.getItem());
    }
    public static @Nullable TrimType getTrimType(@Nullable ItemStack stack) {
        if (stack != null) {
            var trim = get(stack);
            if (trim != null) {
                return trim.trimType();
            }
        }
        return null;
    }

    public static boolean isOfTrimType(@Nullable ItemStack stack, @NotNull TrimType trimType) {
        return getTrimType(stack) == trimType;
    }

    private static final Text UPGRADE_TEXT =
        Text.translatable(Util.createTranslationKey("item", new Identifier("smithing_template.upgrade"))).formatted(Formatting.GRAY);

    public static void appendTooltip(ItemStack stack, List<Text> tooltip) {
        var trimType = getTrimType(stack);
        if (trimType != null) {
            tooltip.add(UPGRADE_TEXT);
            tooltip.add(ScreenTexts.space().append(trimType.getMaterialText()));
            tooltip.add(ScreenTexts.space().append(trimType.getDescriptionText()));
        }
    }

}
