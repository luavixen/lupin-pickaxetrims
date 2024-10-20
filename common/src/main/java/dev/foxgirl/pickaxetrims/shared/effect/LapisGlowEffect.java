package dev.foxgirl.pickaxetrims.shared.effect;

import dev.foxgirl.pickaxetrims.shared.OreDetectUtil;
import dev.foxgirl.pickaxetrims.shared.PickaxeTrimsImpl;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public final class LapisGlowEffect extends AbstractEffect {

    private static final class ShulkerEntityState {
        private final ServerWorld world;
        private final BlockPos pos;
        private final Block block;

        private int ticks;

        private final ShulkerEntity entity;

        public ShulkerEntityState(ServerWorld world, BlockPos pos, Block block) {
            this.world = world;
            this.pos = pos;
            this.block = block;

            resetTicks();

            entity = new ShulkerEntity(EntityType.SHULKER, world);

            entity.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, StatusEffectInstance.INFINITE, 0, false, false));
            updateEntityFlags();

            world.spawnEntity(entity);
        }

        private void resetTicks() {
            ticks = (int) (20 * PickaxeTrimsImpl.getInstance().config.lapisGlowSeconds);
        }

        private void updateEntityFlags() {
            entity.setInvulnerable(true);
            entity.setAiDisabled(true);
            entity.setInvisible(true);
            entity.setGlowing(true);
        }

        private boolean update() {
            if (ticks-- <= 0 || world.getBlockState(pos).getBlock() != block) {
                entity.remove(Entity.RemovalReason.DISCARDED);
                return true;
            } else {
                updateEntityFlags();
                return false;
            }
        }
    }

    private final Map<BlockPos, ShulkerEntityState> activeShulkers = new HashMap<>();

    private void highlightBlock(ServerPlayerEntity player, BlockPos pos, Block block) {
        activeShulkers.computeIfAbsent(pos, __ -> new ShulkerEntityState(player.getServerWorld(), pos, block)).resetTicks();
    }

    private void highlightBlocks(ServerPlayerEntity player, Block expectedBlock) {
        int radius = PickaxeTrimsImpl.getInstance().config.lapisGlowRadius;
        var radiusSquaredDistance = (double) (radius * radius);
        var playerPos = player.getBlockPos().up();
        for (var x = -radius; x <= radius; x++) {
            for (var y = -radius; y <= radius; y++) {
                for (var z = -radius; z <= radius; z++) {
                    var blockPos = playerPos.add(x, y, z);
                    if (blockPos.getSquaredDistance(playerPos) <= radiusSquaredDistance) {
                        var actualBlock = player.getWorld().getBlockState(blockPos).getBlock();
                        if (actualBlock == expectedBlock) {
                            highlightBlock(player, blockPos, expectedBlock);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onTickEnd(@NotNull MinecraftServer server) {
        activeShulkers.values().removeIf(ShulkerEntityState::update);
    }

    @Override
    public void onBlockBreak(@NotNull World level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull ServerPlayerEntity player) {
        var block = state.getBlock();
        if (OreDetectUtil.isOreBlock(block)) {
            highlightBlocks(player, block);
        }
    }

}
