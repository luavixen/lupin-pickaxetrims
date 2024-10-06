package dev.foxgirl.pickaxetrims.client.mixin;

import dev.foxgirl.pickaxetrims.client.PickaxeTrimModels;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.render.model.BlockStatesLoader;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;

@Mixin(ModelLoader.class)
public abstract class MixinModelLoader {

    @Shadow
    private void loadItemModel(ModelIdentifier id) {
        throw new AssertionError();
    }

    @Inject(
        method = "<init>",
        at = @At(
            target = "Lnet/minecraft/client/render/model/ModelLoader;loadItemModel(Lnet/minecraft/client/util/ModelIdentifier;)V",
            value = "INVOKE", ordinal = 0
        )
    )
    private void pickaxetrims$injected$__init__$INVOKE(
        BlockColors blockColors,
        Profiler profiler,
        Map<Identifier, JsonUnbakedModel> jsonUnbakedModels,
        Map<Identifier, List<BlockStatesLoader.SourceTrackedData>> blockStates,
        CallbackInfo info
    ) {
        for (var id : PickaxeTrimModels.getModelIDs()) loadItemModel(id);
    }

}
