package net.blay09.mods.balm.mixin;

import net.blay09.mods.balm.api.block.CustomFarmBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.IPlantable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public class BlockMixin {

    @Inject(method = "canSustainPlant(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;Lnet/minecraftforge/common/IPlantable;)Z", at = @At("HEAD"), remap = false)
    void canSustainPlant(BlockState state, BlockGetter blockGetter, BlockPos pos, Direction direction, IPlantable plantable, CallbackInfoReturnable<Boolean> callbackInfo) {
        if (this instanceof CustomFarmBlock customFarmBlock) {
            Block plant = plantable.getPlant(blockGetter, pos.relative(direction)).getBlock();
            boolean canSustain = customFarmBlock.canSustainPlant(state, blockGetter, pos, direction, plant);
            callbackInfo.setReturnValue(canSustain);
        }
    }

}
