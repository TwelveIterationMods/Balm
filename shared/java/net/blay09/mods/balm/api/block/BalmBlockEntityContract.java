package net.blay09.mods.balm.api.block;

import net.blay09.mods.balm.api.container.BalmContainerProvider;
import net.blay09.mods.balm.api.fluid.BalmFluidTankProvider;
import net.blay09.mods.balm.api.provider.BalmProviderHolder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.AABB;

import java.util.List;

public interface BalmBlockEntityContract {
    AABB balmGetRenderBoundingBox();

    void balmOnLoad();

    void balmFromClientTag(CompoundTag tag);

    CompoundTag balmToClientTag(CompoundTag tag);

    void balmSync();

    default void balmBuildProviders(List<BalmProviderHolder> providers) {
        if (this instanceof BalmContainerProvider containerProvider) {
            providers.add(containerProvider);
        }
        if (this instanceof BalmFluidTankProvider fluidTankProvider) {
            providers.add(fluidTankProvider);
        }
    }
}
