package net.blay09.mods.forbic.client;

import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;

public class ForbicScreens extends ForbicModScreens {

    public static AbstractWidget addRenderableWidget(Screen screen, AbstractWidget widget) {
        Screens.getButtons(screen).add(widget);
        return widget;
    }

}
