package net.blay09.mods.forbic.event;

import net.minecraft.client.gui.screens.Screen;

@FunctionalInterface
public interface ScreenKeyPressedHandler {
    void handle(Screen screen, int key, int scanCode, int modifiers);
}
