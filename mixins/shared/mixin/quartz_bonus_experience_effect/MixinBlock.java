package dev.foxgirl.pickaxetrims.shared.mixin.quartz_bonus_experience_effect;

import com.llamalad7.mixinextras.sugar.Local;
import dev.foxgirl.pickaxetrims.shared.PickaxeTrim;
import dev.foxgirl.pickaxetrims.shared.PickaxeTrimsImpl;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Block.class)
public abstract class MixinBlock {

    @ModifyArg(
        method = "dropExperienceWhenMined(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/item/ItemStack;Lnet/minecraft/util/math/intprovider/IntProvider;)V",
        at = @At(
            target = "Lnet/minecraft/block/Block;dropExperience(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;I)V",
            value = "INVOKE"
        ),
        index = 2
    )
    private int pickaxetrims$injected$dropExperienceWhenMined$INVOKE$dropExperience(
        int experience,
        @Local ServerWorld world,
        @Local ItemStack stack
    ) {
        if (PickaxeTrim.isOfTrimType(stack, PickaxeTrim.TrimType.QUARTZ)) {
            if (experience > 0) {
                float multiplier = MathHelper.nextBetween(
                    world.getRandom(),
                    PickaxeTrimsImpl.getInstance().config.quartzExperienceMultiplierMin,
                    PickaxeTrimsImpl.getInstance().config.quartzExperienceMultiplierMax
                );
                return (int) (experience * multiplier);
            }
        }
        return experience;
    }

}
