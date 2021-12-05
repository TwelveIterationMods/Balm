package net.blay09.mods.balm.mixin;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.block.CustomFarmBlock;
import net.blay09.mods.balm.api.event.CropGrowEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BambooBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Random;

@Mixin(CropBlock.class)
public class CropBlockMixin {

    private static final ThreadLocal<BlockState> getGrowthSpeedBlockState = new ThreadLocal<>();

    @Inject(method = "randomTick(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Ljava/util/Random;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"), cancellable = true)
    public void randomTickPreGrow(BlockState state, ServerLevel level, BlockPos pos, Random random, CallbackInfo callbackInfo) {
        CropGrowEvent.Pre event = new CropGrowEvent.Pre(level, pos, state);
        Balm.getEvents().fireEvent(event);
        if (event.isCanceled()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = "randomTick(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Ljava/util/Random;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z", shift = At.Shift.AFTER))
    public void randomTickPostGrow(BlockState state, ServerLevel level, BlockPos pos, Random random, CallbackInfo callbackInfo) {
        CropGrowEvent.Post event = new CropGrowEvent.Post(level, pos, state);
        Balm.getEvents().fireEvent(event);
    }

    @Inject(method = "mayPlaceOn(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)Z", at = @At("HEAD"), cancellable = true)
    public void mayPlaceOn(BlockState state, BlockGetter blockGetter, BlockPos pos, CallbackInfoReturnable<Boolean> callbackInfo) {
        if (state.getBlock() instanceof CustomFarmBlock customFarmBlock) {
            callbackInfo.setReturnValue(customFarmBlock.canSustainPlant(state, blockGetter, pos, Direction.UP, blockGetter.getBlockState(pos.relative(Direction.UP)).getBlock()));
        }
    }

    @ModifyVariable(method = "getGrowthSpeed(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)F", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"))
    private static BlockState getGrowthSpeedCaptureLocals(BlockState state) {
        getGrowthSpeedBlockState.set(state);
        return state;
    }


    @ModifyVariable(method = "getGrowthSpeed(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)F", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"), ordinal = 1)
    private static float getGrowthSpeed(float g, Block block, BlockGetter blockGetter, BlockPos pos) {
        BlockState state = getGrowthSpeedBlockState.get();
        if (state.getBlock() instanceof CustomFarmBlock customFarmBlock) {
            if (customFarmBlock.canSustainPlant(state, blockGetter, pos, Direction.UP, block)) {
                if (customFarmBlock.isFertile(state, blockGetter, pos)) {
                    return 3f;
                } else {
                    return 1f;
                }
            }
        }

        return g;
    }

}
