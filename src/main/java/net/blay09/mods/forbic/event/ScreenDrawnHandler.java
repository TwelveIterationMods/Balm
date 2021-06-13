package net.blay09.mods.forbic.event;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;

@FunctionalInterface
public interface ScreenDrawnHandler {
    void handle(Screen screen, PoseStack matrixStack, int mouseX, int mouseY);
}
