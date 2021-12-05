package net.blay09.mods.balm.api.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public interface CustomFarmBlock { // TODO Fabric
    boolean canSustainPlant(BlockState state, BlockGetter world, BlockPos pos, Direction facing, Block plant);
    boolean isFertile(BlockState state, BlockGetter world, BlockPos pos);
}
