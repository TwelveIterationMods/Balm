package net.blay09.mods.balm.api.event.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.blay09.mods.balm.api.event.BalmEvent;
import net.minecraft.client.gui.screens.Screen;

public abstract class ContainerScreenDrawEvent extends BalmEvent {
    private final Screen screen;
    private final PoseStack poseStack;
    private final int mouseX;
    private final int mouseY;

    public ContainerScreenDrawEvent(Screen screen, PoseStack poseStack, int mouseX, int mouseY) {
        this.screen = screen;
        this.poseStack = poseStack;
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }

    public Screen getScreen() {
        return screen;
    }

    public PoseStack getPoseStack() {
        return poseStack;
    }

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public static class Background extends ContainerScreenDrawEvent {
        public Background(Screen screen, PoseStack poseStack, int mouseX, int mouseY) {
            super(screen, poseStack, mouseX, mouseY);
        }
    }

    public static class Foreground extends ContainerScreenDrawEvent {
        public Foreground(Screen screen, PoseStack poseStack, int mouseX, int mouseY) {
            super(screen, poseStack, mouseX, mouseY);
        }
    }
}
