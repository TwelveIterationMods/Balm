package net.blay09.mods.balm.api.block.entity;

import net.blay09.mods.balm.api.block.BalmBlockEntityContract;
import net.blay09.mods.balm.api.provider.BalmProviderHolder;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class BalmBlockEntity extends BlockEntity implements BlockEntityClientSerializable, BalmBlockEntityContract {
    public BalmBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    @Override
    public AABB balmGetRenderBoundingBox() {
        // TODO not yet used
        return AABB.unitCubeFromLowerCorner(new Vec3(worldPosition.getX(), worldPosition.getY(), worldPosition.getY()));
    }

    @Override
    public void balmOnLoad() {
    }

    @Override
    public void balmFromClientTag(CompoundTag tag) {
    }

    @Override
    public CompoundTag balmToClientTag(CompoundTag tag) {
        return tag;
    }

    @Override
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
