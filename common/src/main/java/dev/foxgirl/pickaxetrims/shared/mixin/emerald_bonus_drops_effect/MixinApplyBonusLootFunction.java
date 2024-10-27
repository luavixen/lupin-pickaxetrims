package dev.foxgirl.pickaxetrims.shared.mixin.emerald_bonus_drops_effect;

import com.llamalad7.mixinextras.sugar.Local;
import dev.foxgirl.pickaxetrims.shared.PickaxeTrim;
import dev.foxgirl.pickaxetrims.shared.PickaxeTrimsImpl;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.ApplyBonusLootFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ApplyBonusLootFunction.class)
public abstract class MixinApplyBonusLootFunction {

    @Unique
    private void pickaxetrims$processCustomHandler(ItemStack toolStack, ItemStack droppedStack, LootContext context) {
        if (PickaxeTrim.isOfTrimType(toolStack, PickaxeTrim.TrimType.EMERALD)) {
            if (Math.random() <= PickaxeTrimsImpl.getInstance().config.emeraldDoubleDropsChance) {
                droppedStack.setCount(droppedStack.getCount() * 2);
            }
        }
    }

    @Inject(
        method = "process(Lnet/minecraft/item/ItemStack;Lnet/minecraft/loot/context/LootContext;)Lnet/minecraft/item/ItemStack;",
        at = @At(
            target = "Lnet/minecraft/enchantment/EnchantmentHelper;getLevel(Lnet/minecraft/enchantment/Enchantment;Lnet/minecraft/item/ItemStack;)I",
            value = "INVOKE", ordinal = 0, shift = At.Shift.BEFORE
        )
    )
    private void pickaxetrims$injected$process$RETURN(
        ItemStack droppedStack,
        LootContext context,
        CallbackInfoReturnable<ItemStack> info,
        @Local(ordinal = 1) ItemStack toolStack
    ) {
        pickaxetrims$processCustomHandler(toolStack, droppedStack, context);
    }

}
