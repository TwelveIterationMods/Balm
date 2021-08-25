package net.blay09.mods.balm.api.event;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BreakBlockEvent extends BalmEvent {
    private final Level level;
    private final Player player;
    private final BlockPos pos;
    private final BlockState state;
    private final BlockEntity blockEntity;

    public BreakBlockEvent(Level level, Player player, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        this.level = level;
        this.player = player;
        this.pos = pos;
        this.state = state;
        this.blockEntity = blockEntity;
    }

    public Level getLevel() {
        return level;
    }

    public Player getPlayer() {
        return player;
    }

    public BlockPos getPos() {
        return pos;
    }

    public BlockState getState() {
        return state;
    }

    public BlockEntity getBlockEntity() {
        return blockEntity;
    }

    public static class Pre extends BreakBlockEvent {
        public Pre(Level world, Player player, BlockPos pos, BlockState state, BlockEntity blockEntity) {
            super(world, player, pos, state, blockEntity);
        }
    }

    public static class Post extends BreakBlockEvent {
        public Post(Level world, Player player, BlockPos pos, BlockState state, BlockEntity blockEntity) {
            super(world, player, pos, state, blockEntity);
        }
    }
}
