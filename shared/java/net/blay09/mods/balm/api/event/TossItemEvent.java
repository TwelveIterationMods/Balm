package net.blay09.mods.balm.api.event;

import net.minecraft.world.entity.player.Player;

public class TossItemEvent extends BalmEvent {
    private final Player player;

    public TossItemEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
