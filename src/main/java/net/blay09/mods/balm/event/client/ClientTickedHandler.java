package net.blay09.mods.balm.event.client;

import net.minecraft.client.Minecraft;

@FunctionalInterface
public interface ClientTickedHandler {
    void handle(Minecraft client);
}
