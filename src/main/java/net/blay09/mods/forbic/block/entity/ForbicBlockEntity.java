package net.blay09.mods.forbic.block.entity;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class ForbicBlockEntity extends BlockEntity implements BlockEntityClientSerializable {
    public ForbicBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    // TODO not used
    public AABB forbicGetRenderBoundingBox() {
        return AABB.unitCubeFromLowerCorner(new Vec3(worldPosition.getX(), worldPosition.getY(), worldPosition.getY()));
    }

    public void forbicOnLoad() {
    }

    public void forbicFromClientTag(CompoundTag tag) {
    }

    public CompoundTag forbicToClientTag(CompoundTag tag) {
        return tag;
    }

    public void forbicSync() {
        sync();
    }

    @Override
    public void fromClientTag(CompoundTag tag) {
        forbicFromClientTag(tag);
    }

    @Override
    public CompoundTag toClientTag(CompoundTag tag) {
        return forbicToClientTag(tag);
    }
}
