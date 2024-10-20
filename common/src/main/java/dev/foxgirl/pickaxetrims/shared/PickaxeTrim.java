package dev.foxgirl.pickaxetrims.shared;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipAppender;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public record PickaxeTrim(@NotNull RegistryEntry<Item> ingredient, boolean showInTooltip) implements TooltipAppender {

    public static final @NotNull Codec<PickaxeTrim> CODEC = RecordCodecBuilder.create(
        instance -> instance
            .group(
                RegistryFixedCodec.of(RegistryKeys.ITEM).fieldOf("ingredient").forGetter(PickaxeTrim::ingredient),
                Codec.BOOL.optionalFieldOf("show_in_tooltip", true).forGetter(PickaxeTrim::showInTooltip)
            )
            .apply(instance, PickaxeTrim::new)
    );

    public static final @NotNull PacketCodec<RegistryByteBuf, PickaxeTrim> PACKET_CODEC = PacketCodec.tuple(
        PacketCodecs.registryEntry(RegistryKeys.ITEM), PickaxeTrim::ingredient,
        PacketCodecs.BOOL, PickaxeTrim::showInTooltip,
        PickaxeTrim::new
    );

    public enum PickaxeType {
        NETHERITE,
        DIAMOND,
        GOLD,
        IRON;

        public static final int COUNT = values().length;

        private static @Nullable PickaxeType from(@NotNull Item item) {
            if (item == Items.NETHERITE_PICKAXE) return NETHERITE;
            if (item == Items.DIAMOND_PICKAXE) return DIAMOND;
            if (item == Items.GOLDEN_PICKAXE) return GOLD;
            if (item == Items.IRON_PICKAXE) return IRON;
            return null;
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
            public @NotNull Formatting getColor() { return Formatting.DARK_PURPLE; }
        },
        LAPIS_LAZULI {
            public @NotNull String toString() { return "lapis_lazuli"; }
            public @NotNull Formatting getColor() { return Formatting.BLUE; }
        },
        EMERALD {
            public @NotNull String toString() { return "emerald"; }
            public @NotNull Formatting getColor() { return Formatting.GREEN; }
        },
        QUARTZ {
            public @NotNull String toString() { return "quartz"; }
            public @NotNull Formatting getColor() { return Formatting.WHITE; }
        },
        REDSTONE {
            public @NotNull String toString() { return "redstone"; }
            public @NotNull Formatting getColor() { return Formatting.RED; }
        },
        COPPER {
            public @NotNull String toString() { return "copper"; }
            public @NotNull Formatting getColor() { return Formatting.GOLD; }
        };

        public static final int COUNT = values().length;

        private static @Nullable TrimType from(@NotNull Item item) {
            if (item == Items.CRYING_OBSIDIAN) return CRYING_OBSIDIAN;
            if (item == Items.LAPIS_LAZULI) return LAPIS_LAZULI;
            if (item == Items.EMERALD) return EMERALD;
            if (item == Items.QUARTZ) return QUARTZ;
            if (item == Items.REDSTONE) return REDSTONE;
            if (item == Items.COPPER_INGOT) return COPPER;
            return null;
        }

        protected abstract @NotNull Formatting getColor();

        public @NotNull Text getMaterialText() {
            return Text.translatable("pickaxetrims.material." + this).formatted(getColor());
        }
        public @NotNull Text getDescriptionText() {
            return Text.translatable("pickaxetrims.description." + this).formatted(getColor());
        }
    }

    public @Nullable TrimType trimType() {
        return TrimType.from(ingredient.value());
    }

    public static @Nullable PickaxeTrim get(@NotNull ItemStack stack) {
        return stack.get(PickaxeTrimsImpl.getInstance().getTrimComponentType());
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
        Text.translatable(Util.createTranslationKey("item", Identifier.ofVanilla("smithing_template.upgrade"))).formatted(Formatting.GRAY);

    @Override
    public void appendTooltip(Item.TooltipContext context, Consumer<Text> tooltip, TooltipType type) {
        if (showInTooltip) {
            var trimType = trimType();
            if (trimType != null) {
                tooltip.accept(UPGRADE_TEXT);
                tooltip.accept(ScreenTexts.space().append(trimType.getMaterialText()));
                tooltip.accept(ScreenTexts.space().append(trimType.getDescriptionText()));
            }
        }
    }

}
