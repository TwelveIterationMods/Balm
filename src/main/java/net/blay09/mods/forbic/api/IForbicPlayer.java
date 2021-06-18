package net.blay09.mods.forbic.api;

import net.minecraft.nbt.CompoundTag;

public interface IForbicPlayer {

    CompoundTag getForbicData();

    void setForbicData(CompoundTag forbicData);
}
