package net.blay09.mods.balm.api.entity;

import net.minecraft.nbt.CompoundTag;

public interface BalmPlayer {

    CompoundTag getBalmData();

    void setBalmData(CompoundTag tag);

}
