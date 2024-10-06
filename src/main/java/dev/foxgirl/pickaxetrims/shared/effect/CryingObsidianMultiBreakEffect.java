package dev.foxgirl.pickaxetrims.shared.effect;

import dev.foxgirl.pickaxetrims.shared.PickaxeTrimsImpl;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public final class CryingObsidianMultiBreakEffect extends AbstractEffect{

    @Override
    public void onTickEnd(@NotNull MinecraftServer server) {
    }

    private void breakIfMatches(ServerPlayerEntity player, World level, Block block, BlockPos pos) {
        if (level.getBlockState(pos).getBlock() == block) {
            level.breakBlock(pos, true, player);
        }
    }

    @Override
    public void onBlockBreak(@NotNull World level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull ServerPlayerEntity player) {
        int radius = PickaxeTrimsImpl.getInstance().config.cryingObsidianMultiBreakRadius;
        var block = state.getBlock();
        for (var x = -radius; x <= radius; x++) {
            for (var y = -radius; y <= radius; y++) {
                for (var z = -radius; z <= radius; z++) {
                    breakIfMatches(player, level, block, pos.add(x, y, z));
                }
            }
        }
    }

}
