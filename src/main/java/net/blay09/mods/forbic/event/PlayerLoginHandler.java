package net.blay09.mods.forbic.event;

import net.minecraft.server.level.ServerPlayer;

@FunctionalInterface
public interface PlayerLoginHandler {
    void handle(ServerPlayer player);
}
