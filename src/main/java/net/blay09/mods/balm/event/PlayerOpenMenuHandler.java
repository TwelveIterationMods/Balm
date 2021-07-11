package net.blay09.mods.balm.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;

@FunctionalInterface
public interface PlayerOpenMenuHandler {
    void handle(ServerPlayer player, AbstractContainerMenu menu);
}
