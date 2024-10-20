package dev.foxgirl.pickaxetrims.shared.mixin.copper_durability_effect;

import dev.foxgirl.pickaxetrims.shared.PickaxeTrim;
import dev.foxgirl.pickaxetrims.shared.PickaxeTrimsImpl;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class MixinItemStack {

    @Inject(method = "getMaxDamage()I", at = @At("RETURN"), cancellable = true)
    private void pickaxetrims$injected$getMaxDamage$RETURN(CallbackInfoReturnable<Integer> info) {
        if (PickaxeTrim.isOfTrimType((ItemStack) (Object) this, PickaxeTrim.TrimType.COPPER)) {
            var multiplier = PickaxeTrimsImpl.getInstance().config.copperDurabilityMultiplier;
            int durability = info.getReturnValue();
            info.setReturnValue((int) (durability * multiplier));
        }
    }

}
