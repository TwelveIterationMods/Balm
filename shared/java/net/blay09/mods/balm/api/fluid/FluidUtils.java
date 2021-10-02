package net.blay09.mods.balm.api.fluid;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class FluidUtils {
    public static boolean useFluidTank(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof BalmFluidTankProvider fluidTankProvider) {
            return useFluidTank(player, hand, fluidTankProvider.getFluidTank());
        }

        return false;
    }

    public static boolean useFluidTank(Player player, InteractionHand hand, FluidTank fluidTank) {
        return false;
    }
}
