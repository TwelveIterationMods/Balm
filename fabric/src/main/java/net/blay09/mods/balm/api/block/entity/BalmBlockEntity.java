package net.blay09.mods.balm.api.block.entity;

import net.blay09.mods.balm.api.block.BalmBlockEntityContract;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class BalmBlockEntity extends BlockEntity implements BalmBlockEntityContract {
    public BalmBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    @Override
    public AABB balmGetRenderBoundingBox() {
        // TODO Block Entity Bounding Box (Not yet called)
        return AABB.unitCubeFromLowerCorner(new Vec3(worldPosition.getX(), worldPosition.getY(), worldPosition.getY()));
    }

    @Override
    @Nullable
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this, BalmBlockEntityContract::toUpdateTag);
    }
}
