package net.blay09.mods.forbic.mixin;

import net.minecraft.nbt.CompoundTag;

public interface IPlayerMixin {

    CompoundTag getForbicData();

    void setForbicData(CompoundTag forbicData);
}
