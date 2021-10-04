package net.blay09.mods.balm.api.block;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.blay09.mods.balm.api.container.BalmContainerProvider;
import net.blay09.mods.balm.api.energy.BalmEnergyStorageProvider;
import net.blay09.mods.balm.api.energy.EnergyStorage;
import net.blay09.mods.balm.api.fluid.BalmFluidTankProvider;
import net.blay09.mods.balm.api.fluid.FluidTank;
import net.blay09.mods.balm.api.provider.BalmProvider;
import net.blay09.mods.balm.api.provider.BalmProviderHolder;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public interface BalmBlockEntityContract extends BalmProviderHolder {
    AABB balmGetRenderBoundingBox();

    void balmOnLoad();

    void balmFromClientTag(CompoundTag tag);

    CompoundTag balmToClientTag(CompoundTag tag);

    void balmSync();

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
