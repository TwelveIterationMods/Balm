package net.blay09.mods.balm.event.client.screen;

import net.minecraft.client.gui.screens.Screen;

@FunctionalInterface
public interface ScreenMouseClickHandler {
    boolean handle(Screen screen, double mouseX, double mouseY, int button);
}
