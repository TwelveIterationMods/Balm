package net.blay09.mods.balm.event.server;

import net.minecraft.server.MinecraftServer;

@FunctionalInterface
public interface ServerStoppedHandler {
    void handle(MinecraftServer server);
}
