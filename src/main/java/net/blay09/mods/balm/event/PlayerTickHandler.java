package net.blay09.mods.balm.event;

import net.minecraft.server.level.ServerPlayer;

public interface PlayerTickHandler {
    void handle(ServerPlayer player);
}
