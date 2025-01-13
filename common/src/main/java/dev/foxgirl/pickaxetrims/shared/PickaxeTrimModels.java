package dev.foxgirl.pickaxetrims.shared;

import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public final class PickaxeTrimModels {

    private static final Identifier[] IDS = new Identifier[PickaxeTrim.PickaxeType.COUNT * PickaxeTrim.TrimType.COUNT];

    private static int calculateIndex(PickaxeTrim.PickaxeType pickaxeType, PickaxeTrim.TrimType trimType) {
        return pickaxeType.ordinal() * PickaxeTrim.TrimType.COUNT + trimType.ordinal();
    }

    static {
        for (var pickaxeType : PickaxeTrim.PickaxeType.VALUES) {
            for (var trimType : PickaxeTrim.TrimType.VALUES) {
                IDS[calculateIndex(pickaxeType, trimType)] =
                    Identifier.of("pickaxetrims", "placeholder_" + pickaxeType + "_trimmed_" + trimType);
            }
        }
    }

    public static @NotNull Identifier get(@NotNull PickaxeTrim.PickaxeType pickaxeType, @NotNull PickaxeTrim.TrimType trimType) {
        return IDS[calculateIndex(pickaxeType, trimType)];
    }

    public static void forEach(@NotNull Consumer<@NotNull Identifier> consumer) {
        for (var id : IDS) {
            consumer.accept(id);
        }
    }

}
