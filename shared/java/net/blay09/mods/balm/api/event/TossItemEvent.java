package net.blay09.mods.balm.api.event;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;

public class TossItemEvent extends BalmEvent {
    private final Player player;
    private final ItemEntity itemEntity;

    public TossItemEvent(Player player, ItemEntity itemEntity) {
        this.player = player;
        this.itemEntity = itemEntity;
    }

    public Player getPlayer() {
        return player;
    }

    public ItemEntity getItemEntity() {
        return itemEntity;
    }
}
