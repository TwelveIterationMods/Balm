package net.blay09.mods.balm.api.block.entity;

import net.blay09.mods.balm.api.block.BalmBlockEntityContract;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BalmBlockEntity extends BlockEntity implements BalmBlockEntityContract {
    public BalmBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    @Override
    public AABB getRenderBoundingBox() {
        return balmGetRenderBoundingBox();
    }

    @Override
    public AABB balmGetRenderBoundingBox() {
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
            List<? extends Player> playerList = level.players();
            ClientboundBlockEntityDataPacket updatePacket = getUpdatePacket();
            if (updatePacket == null) {
                return;
            }

            for (Object obj : playerList) {
                ServerPlayer player = (ServerPlayer) obj;
                if (Math.hypot(player.getX() - worldPosition.getX() + 0.5, player.getZ() - worldPosition.getZ() + 0.5) < 64) {
                    player.connection.send(updatePacket);
                }
            }
        }
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return new ClientboundBlockEntityDataPacket(worldPosition, 0, getUpdateTag());
    }

    @Override
    public CompoundTag getUpdateTag() {
        return balmToClientTag(new CompoundTag());
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        super.onDataPacket(net, pkt);

        balmFromClientTag(pkt.getTag());
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        super.handleUpdateTag(tag);

        balmFromClientTag(tag);
    }

}
