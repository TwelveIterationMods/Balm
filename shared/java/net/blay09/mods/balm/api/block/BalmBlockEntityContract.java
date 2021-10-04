package net.blay09.mods.balm.api.block;

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

    void balmBuildProviders(List<BalmProviderHolder> providers);
}
