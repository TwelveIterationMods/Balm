package net.blay09.mods.forbic.event;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

@FunctionalInterface
public interface ConfigReloadedHandler {
    void handle(MinecraftServer server);
}
