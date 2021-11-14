package net.blay09.mods.balm.api.event.client;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import net.blay09.mods.balm.api.event.BalmEvent;

public abstract class GuiDrawEvent extends BalmEvent {

    public enum Element {
        ALL,
        HEALTH,
        CHAT,
        DEBUG,
        BOSS_INFO,
        PLAYER_LIST
    }

    private final Window window;
    private final PoseStack poseStack;
    private final Element element;

    public GuiDrawEvent(Window window, PoseStack poseStack, Element element) {
        this.window = window;
        this.poseStack = poseStack;
        this.element = element;
    }

    public Window getWindow() {
        return window;
    }

    public PoseStack getPoseStack() {
        return poseStack;
    }

    public Element getElement() {
        return element;
    }

    public static class Pre extends GuiDrawEvent {
        public Pre(Window window, PoseStack poseStack, Element element) {
            super(window, poseStack, element);
        }
    }

    public static class Post extends GuiDrawEvent {
        public Post(Window window, PoseStack poseStack, Element element) {
            super(window, poseStack, element);
        }
    }
}
