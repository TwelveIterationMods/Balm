package net.blay09.mods.balm.event.client.screen;

import net.minecraft.client.gui.screens.Screen;

@FunctionalInterface
public interface ScreenInitializedHandler {
    void handle(Screen screen);
}
