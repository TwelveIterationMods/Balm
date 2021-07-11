package net.blay09.mods.balm.api;

import net.minecraft.nbt.CompoundTag;

public interface IBalmPlayer {

    CompoundTag getBalmData();

    void setBalmData(CompoundTag tag);
}
