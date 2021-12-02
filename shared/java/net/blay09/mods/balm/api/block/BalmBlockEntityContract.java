package net.blay09.mods.balm.api.block;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.blay09.mods.balm.api.block.entity.BalmBlockEntity;
import net.blay09.mods.balm.api.container.BalmContainerProvider;
import net.blay09.mods.balm.api.energy.BalmEnergyStorageProvider;
import net.blay09.mods.balm.api.energy.EnergyStorage;
import net.blay09.mods.balm.api.fluid.BalmFluidTankProvider;
import net.blay09.mods.balm.api.fluid.FluidTank;
import net.blay09.mods.balm.api.provider.BalmProvider;
import net.blay09.mods.balm.api.provider.BalmProviderHolder;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public interface BalmBlockEntityContract extends BalmProviderHolder {
    default CompoundTag balmToClientTag(CompoundTag tag) {
        return tag;
    }

    default void balmSync() {
        BlockEntity self = (BlockEntity) this;
        if (self.getLevel() != null && !self.getLevel().isClientSide) {
            ((ServerLevel) self.getLevel()).getChunkSource().blockChanged(self.getBlockPos());
        }
    }

    default Packet<ClientGamePacketListener>  createUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(((BlockEntity) this), BalmBlockEntityContract::toUpdateTag);
    }

    static CompoundTag toUpdateTag(BlockEntity blockEntity) {
        if (blockEntity instanceof BalmBlockEntityContract balmBlockEntity) {
            return balmBlockEntity.balmToClientTag(new CompoundTag());
        }

        return new CompoundTag();
    }

    default void balmBuildProviders(List<BalmProviderHolder> providers) {
        providers.add(this);

        if (this instanceof BalmContainerProvider containerProvider) {
            providers.add(new BalmProviderHolder() {
                @Override
                public List<BalmProvider<?>> getProviders() {
                    Container container = containerProvider.getContainer();
                    if (container != null) {
                        return Lists.newArrayList(new BalmProvider<>(Container.class, container));
                    }

                    return Collections.emptyList();
                }

                @Override
                public List<Pair<Direction, BalmProvider<?>>> getSidedProviders() {
                    List<Pair<Direction, BalmProvider<?>>> providers = new ArrayList<>();
                    for (Direction direction : Direction.values()) {
                        Container container = containerProvider.getContainer(direction);
                        if (container != null) {
                            providers.add(Pair.of(direction, new BalmProvider<>(Container.class, container)));
                        }
                    }
                    return providers;
                }
            });
        }

        if (this instanceof BalmFluidTankProvider fluidTankProvider) {
            providers.add(new BalmProviderHolder() {
                @Override
                public List<BalmProvider<?>> getProviders() {
                    FluidTank fluidTank = fluidTankProvider.getFluidTank();
                    if (fluidTank != null) {
                        return Lists.newArrayList(new BalmProvider<>(FluidTank.class, fluidTank));
                    }

                    return Collections.emptyList();
                }

                @Override
                public List<Pair<Direction, BalmProvider<?>>> getSidedProviders() {
                    List<Pair<Direction, BalmProvider<?>>> providers = new ArrayList<>();
                    for (Direction direction : Direction.values()) {
                        FluidTank fluidTank = fluidTankProvider.getFluidTank(direction);
                        if (fluidTank != null) {
                            providers.add(Pair.of(direction, new BalmProvider<>(FluidTank.class, fluidTank)));
                        }
                    }
                    return providers;
                }
            });
        }

        if (this instanceof BalmEnergyStorageProvider energyStorageProvider) {
            providers.add(new BalmProviderHolder() {
                @Override
                public List<BalmProvider<?>> getProviders() {
                    EnergyStorage energyStorage = energyStorageProvider.getEnergyStorage();
                    if (energyStorage != null) {
                        return Lists.newArrayList(new BalmProvider<>(EnergyStorage.class, energyStorage));
                    }

                    return Collections.emptyList();
                }

                @Override
                public List<Pair<Direction, BalmProvider<?>>> getSidedProviders() {
                    List<Pair<Direction, BalmProvider<?>>> providers = new ArrayList<>();
                    for (Direction direction : Direction.values()) {
                        EnergyStorage energyStorage = energyStorageProvider.getEnergyStorage(direction);
                        if (energyStorage != null) {
                            providers.add(Pair.of(direction, new BalmProvider<>(EnergyStorage.class, energyStorage)));
                        }
                    }
                    return providers;
                }
            });
        }
    }
}
