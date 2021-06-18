package net.blay09.mods.forbic.entity;

import net.blay09.mods.forbic.mixin.IPlayerMixin;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

public class ForbicPlayer {

    /**
     * Forge provides a tag in player data that is persisted across clones and death.
     * Fabric does not provide such a tag; so we add our own
     */
    public static CompoundTag getPersistentData(Player player) {
        return ((IPlayerMixin) player).getForbicData();
    }
}
