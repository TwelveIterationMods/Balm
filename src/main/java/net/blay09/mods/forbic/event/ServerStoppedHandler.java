package net.blay09.mods.forbic.event;

import net.minecraft.server.MinecraftServer;

@FunctionalInterface
public interface ServerStoppedHandler {
    void handle(MinecraftServer server);
}
