package net.blay09.mods.balm.api.event;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class PlayerChangedDimensionEvent extends BalmEvent {
    private final Player player;
    private final ResourceKey<Level> fromDim;
    private final ResourceKey<Level> toDim;

    public PlayerChangedDimensionEvent(Player player, ResourceKey<Level> fromDim, ResourceKey<Level> toDim) {
        this.player = player;
        this.fromDim = fromDim;
        this.toDim = toDim;
    }

    public Player getPlayer() {
        return player;
    }

    public ResourceKey<Level> getFromDim() {
        return fromDim;
    }

    public ResourceKey<Level> getToDim() {
        return toDim;
    }
}
