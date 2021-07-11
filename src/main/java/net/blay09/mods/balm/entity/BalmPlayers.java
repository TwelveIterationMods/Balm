package net.blay09.mods.balm.entity;

import net.blay09.mods.balm.api.IBalmPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

public class BalmPlayers {

    /**
     * Forge provides a tag in player data that is persisted across clones and death.
     * Fabric does not provide such a tag; so we add our own
     */
    public static CompoundTag getPersistentData(Player player) {
        return ((IBalmPlayer) player).getBalmData();
    }
}
