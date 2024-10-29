package dev.foxgirl.pickaxetrims.client.mixin;

import dev.foxgirl.pickaxetrims.client.PickaxeTrimModels;
import net.minecraft.client.render.item.ItemModels;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemRenderer.class)
public abstract class MixinItemRenderer {

    @Shadow @Final
    private ItemModels models;

    @Inject(
        method = "getModel(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;I)Lnet/minecraft/client/render/model/BakedModel;",
        at = @At("RETURN"), cancellable = true
    )
    public void pickaxetrims$injected$getModel$RETURN(ItemStack stack, World world, LivingEntity entity, int seed, CallbackInfoReturnable<BakedModel> info) {
        if (stack.getItem() instanceof PickaxeItem) {
            var id = PickaxeTrimModels.getModelID(stack);
            if (id != null) {
                info.setReturnValue(models.getModelManager().getModel(id));
            }
        }
    }

}
