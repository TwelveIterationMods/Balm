package net.blay09.mods.balm.api.event.server;

import net.blay09.mods.balm.api.event.BalmEvent;
import net.minecraft.server.ServerResources;

public class ServerReloadedEvent extends BalmEvent {
    private final ServerResources resources;

    public ServerReloadedEvent(ServerResources resources) {
        this.resources = resources;
    }

    public ServerResources getResources() {
        return resources;
    }
}
