package net.blay09.mods.balm.api.event.server;

import net.blay09.mods.balm.api.event.BalmEvent;
import net.minecraft.server.MinecraftServer;

public class ServerReloadedEvent extends BalmEvent {
    private final MinecraftServer server;

    public ServerReloadedEvent(MinecraftServer server) {
        this.server = server;
    }

    public MinecraftServer getServer() {
        return server;
    }
}
