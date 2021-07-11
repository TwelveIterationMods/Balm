package net.blay09.mods.balm.block.entity;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class BalmBlockEntity extends BlockEntity implements BlockEntityClientSerializable {
    public BalmBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    // TODO not used
    public AABB balmGetRenderBoundingBox() {
        return AABB.unitCubeFromLowerCorner(new Vec3(worldPosition.getX(), worldPosition.getY(), worldPosition.getY()));
    }

    public void balmOnLoad() {
    }

    public void balmFromClientTag(CompoundTag tag) {
    }

    public CompoundTag balmToClientTag(CompoundTag tag) {
        return tag;
    }

    public void balmSync() {
        if (level != null && !level.isClientSide) {
            sync();
        }
    }

    @Override
    public void fromClientTag(CompoundTag tag) {
        balmFromClientTag(tag);
    }

    @Override
    public CompoundTag toClientTag(CompoundTag tag) {
        return balmToClientTag(tag);
    }
}
