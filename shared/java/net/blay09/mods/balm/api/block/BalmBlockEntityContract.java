package net.blay09.mods.balm.api.block;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.AABB;

public interface BalmBlockEntityContract {
    AABB balmGetRenderBoundingBox();

    void balmOnLoad();

    void balmFromClientTag(CompoundTag tag);

    CompoundTag balmToClientTag(CompoundTag tag);

    void balmSync();
}
