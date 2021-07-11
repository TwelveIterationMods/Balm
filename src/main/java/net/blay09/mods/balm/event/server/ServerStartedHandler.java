package net.blay09.mods.balm.event.server;

import net.minecraft.server.MinecraftServer;

@FunctionalInterface
public interface ServerStartedHandler {
    void handle(MinecraftServer server);
}
