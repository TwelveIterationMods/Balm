package net.blay09.mods.forbic;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class ForbicBlockEntity extends BlockEntity {
    public ForbicBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    public AABB getRenderBoundingBox() {
        return AABB.unitCubeFromLowerCorner(new Vec3(worldPosition.getX(), worldPosition.getY(), worldPosition.getY()));
    }
}
