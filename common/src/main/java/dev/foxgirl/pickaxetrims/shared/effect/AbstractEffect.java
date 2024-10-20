package dev.foxgirl.pickaxetrims.shared.effect;

import net.minecraft.block.BlockState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
public abstract class AbstractEffect {

    public abstract void onTickEnd(@NotNull MinecraftServer server);

    public abstract void onBlockBreak(
        @NotNull World level,
        @NotNull BlockPos pos,
        @NotNull BlockState state,
        @NotNull ServerPlayerEntity player
    );

}
