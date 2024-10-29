package dev.foxgirl.pickaxetrims.client.mixin;

import dev.foxgirl.pickaxetrims.client.PickaxeTrimModels;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.util.ModelIdentifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModelLoader.class)
public abstract class MixinModelLoader {

    @Shadow
    private void addModel(ModelIdentifier id) {
        throw new AssertionError();
    }

    @Unique
    private void pickaxetrims$addModels() {
        for (var id : PickaxeTrimModels.getModelIDs()) addModel(id);
    }

    @Inject(
        method = "addModel(Lnet/minecraft/client/util/ModelIdentifier;)V",
        at = @At("HEAD")
    )
    private void pickaxetrims$injected$addModel$HEAD(ModelIdentifier id, CallbackInfo info) {
        if (id.equals(ItemRenderer.SPYGLASS_IN_HAND)) pickaxetrims$addModels();
    }

}
