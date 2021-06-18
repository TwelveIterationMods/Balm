package net.blay09.mods.forbic.event;

import net.minecraft.server.MinecraftServer;

@FunctionalInterface
public interface ServerStartedHandler {
    void handle(MinecraftServer server);
}
