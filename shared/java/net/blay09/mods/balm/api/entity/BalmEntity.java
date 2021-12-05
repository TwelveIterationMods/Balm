package net.blay09.mods.balm.api.entity;

import net.minecraft.nbt.CompoundTag;

public interface BalmEntity {

    CompoundTag getBalmData();

    void setBalmData(CompoundTag tag);

}
