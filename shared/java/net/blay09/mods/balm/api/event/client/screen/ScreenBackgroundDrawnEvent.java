package net.blay09.mods.balm.api.event.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.blay09.mods.balm.api.event.BalmEvent;
import net.minecraft.client.gui.screens.Screen;

public class ScreenBackgroundDrawnEvent extends BalmEvent {
    private final Screen screen;
    private final PoseStack poseStack;

    public ScreenBackgroundDrawnEvent(Screen screen, PoseStack poseStack) {
        this.screen = screen;
        this.poseStack = poseStack;
    }

    public Screen getScreen() {
        return screen;
    }

    public PoseStack getPoseStack() {
        return poseStack;
    }
}
