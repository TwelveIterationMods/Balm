package net.blay09.mods.balm.api.event.server;

import net.blay09.mods.balm.api.event.BalmEvent;
import net.blay09.mods.balm.mixin.MinecraftServerAccessor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerResources;

public class ServerReloadedEvent extends BalmEvent {
    private final MinecraftServer server;
    private final ServerResources resources;

    public ServerReloadedEvent(MinecraftServer server) {
        this.server = server;
        this.resources = ((MinecraftServerAccessor) server).getResources();
    }

    public MinecraftServer getServer() {
        return server;
    }

    public ServerResources getResources() {
        return resources;
    }
}
