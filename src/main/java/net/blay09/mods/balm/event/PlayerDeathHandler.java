package net.blay09.mods.balm.event;

import net.minecraft.server.level.ServerPlayer;

@FunctionalInterface
public interface PlayerDeathHandler {
    void handle(ServerPlayer player);
}
