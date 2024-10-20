package dev.foxgirl.pickaxetrims.shared.mixin.quartz_bonus_experience_effect;

import dev.foxgirl.pickaxetrims.shared.PickaxeTrim;
import dev.foxgirl.pickaxetrims.shared.PickaxeTrimsImpl;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentHelper.class)
public abstract class MixinEnchantmentHelper {

    @Inject(
        method = "getBlockExperience(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/item/ItemStack;I)I",
        at = @At("RETURN"), cancellable = true
    )
    private static void pickaxetrims$injected$getBlockExperience$RETURN(
        ServerWorld world,
        ItemStack stack,
        int baseBlockExperience,
        CallbackInfoReturnable<Integer> info
    ) {
        if (PickaxeTrim.isOfTrimType(stack, PickaxeTrim.TrimType.QUARTZ)) {
            int experience = info.getReturnValueI();
            if (experience > 0) {
                float multiplier = MathHelper.nextBetween(
                    world.getRandom(),
                    PickaxeTrimsImpl.getInstance().config.quartzExperienceMultiplierMin,
                    PickaxeTrimsImpl.getInstance().config.quartzExperienceMultiplierMax
                );
                info.setReturnValue((int) (experience * multiplier));
            }
        }
    }

}
