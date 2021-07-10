package net.blay09.mods.forbic.event;

import net.minecraft.client.Minecraft;

@FunctionalInterface
public interface ClientTickedHandler {
    void handle(Minecraft client);
}
