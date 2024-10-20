package dev.foxgirl.pickaxetrims.shared.effect;

import dev.foxgirl.pickaxetrims.shared.OreDetectUtil;
import dev.foxgirl.pickaxetrims.shared.PickaxeTrimsImpl;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public final class RedstoneVeinMineEffect extends AbstractEffect {

    private void findPositions(BlockPos pos, Block block, World level, ServerPlayerEntity player, List<BlockPos> positions, int depth) {
        if (depth-- < 0) {
            return;
        }
        if (block != level.getBlockState(pos).getBlock()) {
            return;
        }
        if (!positions.contains(pos)) {
            positions.add(pos);
            findPositions(pos.north(), block, level, player, positions, depth);
            findPositions(pos.south(), block, level, player, positions, depth);
            findPositions(pos.east(), block, level, player, positions, depth);
            findPositions(pos.west(), block, level, player, positions, depth);
            findPositions(pos.up(), block, level, player, positions, depth);
            findPositions(pos.down(), block, level, player, positions, depth);
        }
    }

    private void sortPositions(BlockPos pos, List<BlockPos> positions) {
        positions.sort((a, b) -> {
            var aDistance = a.getSquaredDistance(pos);
            var bDistance = b.getSquaredDistance(pos);
            return Double.compare(aDistance, bDistance);
        });
    }

    private final Queue<Runnable> pendingTasks = new ArrayDeque<>();

    @Override
    public void onTickEnd(@NotNull MinecraftServer server) {
        var task = pendingTasks.poll();
        if (task != null) task.run();
    }

    @Override
    public void onBlockBreak(@NotNull World level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull ServerPlayerEntity player) {
        var block = state.getBlock();
        if (OreDetectUtil.isOreBlock(block)) {
            List<BlockPos> positions = new ArrayList<>();
            findPositions(pos, block, level, player, positions, PickaxeTrimsImpl.getInstance().config.redstoneVeinMineDepth);
            sortPositions(pos, positions);
            positions.stream().<Runnable>map((it) -> () -> level.breakBlock(it, true, player)).forEach(pendingTasks::add);
        }
    }

}
