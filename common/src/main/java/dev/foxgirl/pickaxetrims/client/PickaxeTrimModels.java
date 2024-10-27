package dev.foxgirl.pickaxetrims.client;

import dev.foxgirl.pickaxetrims.shared.PickaxeTrim;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public final class PickaxeTrimModels {

    private static final ModelIdentifier[] MODEL_IDS = new ModelIdentifier[PickaxeTrim.PickaxeType.COUNT * PickaxeTrim.TrimType.COUNT];

    private static int calculateIndex(PickaxeTrim.PickaxeType pickaxeType, PickaxeTrim.TrimType trimType) {
        return pickaxeType.ordinal() * PickaxeTrim.TrimType.COUNT + trimType.ordinal();
    }

    static {
        for (var pickaxeType : PickaxeTrim.PickaxeType.VALUES) {
            for (var trimType : PickaxeTrim.TrimType.VALUES) {
                var baseID = Identifier.of("pickaxetrims", pickaxeType + "_trimmed_" + trimType);
                var modelInventoryID = new ModelIdentifier(baseID, "inventory");
                MODEL_IDS[calculateIndex(pickaxeType, trimType)] = modelInventoryID;
            }
        }
    }

    public static @NotNull ModelIdentifier getModelID(@NotNull PickaxeTrim.PickaxeType pickaxeType, @NotNull PickaxeTrim.TrimType trimType) {
        return MODEL_IDS[calculateIndex(pickaxeType, trimType)];
    }

    public static @Nullable ModelIdentifier getModelID(@NotNull ItemStack stack) {
        var pickaxeType = PickaxeTrim.getPickaxeType(stack);
        if (pickaxeType == null) return null;
        var trimType = PickaxeTrim.getTrimType(stack);
        if (trimType == null) return null;
        return getModelID(pickaxeType, trimType);
    }

    public static @NotNull List<ModelIdentifier> getModelIDs() {
        return Arrays.asList(MODEL_IDS);
    }

}
