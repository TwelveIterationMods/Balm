package net.blay09.mods.balm.event;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public interface BlockBrokenHandler {
    void handle(Level world, Player player, BlockPos pos, BlockState state, BlockEntity blockEntity);
}
