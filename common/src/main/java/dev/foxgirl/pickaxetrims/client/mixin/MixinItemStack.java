package dev.foxgirl.pickaxetrims.client.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import dev.foxgirl.pickaxetrims.shared.PickaxeTrimsImpl;
import net.minecraft.component.ComponentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipAppender;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class MixinItemStack {

    @Shadow
    private <T extends TooltipAppender> void appendTooltip(
        ComponentType<T> componentType, Item.TooltipContext context, Consumer<Text> textConsumer, TooltipType type
    ) {
        throw new AssertionError();
    }

    @Inject(
        method = "getTooltip(Lnet/minecraft/item/Item$TooltipContext;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/tooltip/TooltipType;)Ljava/util/List;",
        at = @At(
            target = "Lnet/minecraft/item/ItemStack;appendTooltip(Lnet/minecraft/component/ComponentType;Lnet/minecraft/item/Item$TooltipContext;Ljava/util/function/Consumer;Lnet/minecraft/item/tooltip/TooltipType;)V",
            value = "INVOKE", ordinal = 2
        )
    )
    private void pickaxetrims$injected$getTooltip$INVOKE(
        Item.TooltipContext context,
        @Nullable PlayerEntity player,
        TooltipType type,
        CallbackInfoReturnable<List<Text>> info,
        @Local Consumer<Text> consumer
    ) {
        appendTooltip(PickaxeTrimsImpl.getInstance().getTrimComponentType(), context, consumer, type);
    }

}
