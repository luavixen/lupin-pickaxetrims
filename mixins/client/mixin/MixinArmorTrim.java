package dev.foxgirl.pickaxetrims.client.mixin;

import dev.foxgirl.pickaxetrims.shared.PickaxeTrim;
import net.minecraft.item.ItemStack;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ArmorTrim.class)
public abstract class MixinArmorTrim {

    @Inject(
        method = "appendTooltip(Lnet/minecraft/item/ItemStack;Lnet/minecraft/registry/DynamicRegistryManager;Ljava/util/List;)V",
        at = @At("RETURN")
    )
    private static void pickaxetrims$injected$appendTooltip(
        ItemStack stack,
        DynamicRegistryManager registryManager,
        List<Text> tooltip,
        CallbackInfo info
    ) {
        PickaxeTrim.appendTooltip(stack, tooltip);
    }

}
