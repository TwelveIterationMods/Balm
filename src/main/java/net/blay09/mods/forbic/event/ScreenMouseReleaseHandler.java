package net.blay09.mods.forbic.event;

import net.minecraft.client.gui.screens.Screen;

@FunctionalInterface
public interface ScreenMouseReleaseHandler {
    void handle(Screen screen, double mouseX, double mouseY, int button);
}
