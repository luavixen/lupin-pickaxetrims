package dev.foxgirl.pickaxetrims.shared.mixin;

import dev.foxgirl.pickaxetrims.shared.PickaxeTrim;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.SmithingRecipe;
import net.minecraft.recipe.SmithingTransformRecipe;
import net.minecraft.registry.DynamicRegistryManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(SmithingTransformRecipe.class)
public abstract class MixinSmithingTransformRecipe implements SmithingRecipe {

    @Shadow @Final
    private Ingredient addition;

    @Unique
    private void pickaxetrims$modifyPickaxeTrimCraftingResult(ItemStack result) {
        var ingredientStacks = addition.getMatchingStacks();
        if (ingredientStacks == null || ingredientStacks.length < 1) {
            throw new IllegalStateException("Invalid SmithingTransformRecipe 'addition' value for pickaxe trim recipe, no matching stacks");
        }
        var ingredientStack = Objects.requireNonNull(ingredientStacks[0], "Expression 'ingredientStacks[0]' is null");
        var ingredientItem = ingredientStack.getItem();
        var trimType = PickaxeTrim.TrimType.from(ingredientItem);
        if (trimType == null) {
            throw new IllegalStateException("Invalid SmithingTransformRecipe 'addition' value for pickaxe trim recipe, item of first matching stack is not a trim item: " + ingredientItem);
        }
        PickaxeTrim.set(result, trimType);
    }

    @Inject(
        method = "craft(Lnet/minecraft/inventory/Inventory;Lnet/minecraft/registry/DynamicRegistryManager;)Lnet/minecraft/item/ItemStack;",
        at = @At("RETURN")
    )
    private void pickaxetrims$injected$craft$RETURN(
        Inventory inventory,
        DynamicRegistryManager registryManager,
        CallbackInfoReturnable<ItemStack> info
    ) {
        if (getId().getPath().contains("fracture_armor_trim_smithing_template")) {
            pickaxetrims$modifyPickaxeTrimCraftingResult(info.getReturnValue());
        }
    }

}
