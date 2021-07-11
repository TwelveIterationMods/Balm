package net.blay09.mods.balm.event.client.screen;

import net.minecraft.client.gui.screens.Screen;

@FunctionalInterface
public interface ScreenKeyPressedHandler {
    boolean handle(Screen screen, int key, int scanCode, int modifiers);
}
