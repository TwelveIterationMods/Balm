package net.blay09.mods.balm.event.client;

import net.blay09.mods.balm.event.client.screen.*;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenKeyboardEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

import java.util.ArrayList;
import java.util.List;

public class BalmClientEvents {

    private static final List<ScreenDrawnHandler> screenDrawnHandlers = new ArrayList<>();
    private static final List<ScreenKeyPressedHandler> screenKeyPressedHandlers = new ArrayList<>();
    private static final List<ScreenMouseClickHandler> screenMouseClickHandlers = new ArrayList<>();
    private static final List<ScreenMouseReleaseHandler> screenMouseReleaseHandlers = new ArrayList<>();
    private static ScreenEvents.BeforeInit beforeInitListener = null;

    public static Event<FovUpdateHandler> FOV_UPDATE = EventFactory.createArrayBacked(FovUpdateHandler.class,
            (listeners) -> (entity) -> {
                for (FovUpdateHandler listener : listeners) {
                    Float fov = listener.handle(entity);
                    if (fov != null) {
                        return fov;
                    }
                }

                return null;
            });

    public static void onConnectedToServer(ConnectedToServerHandler handler) {
        ClientPlayConnectionEvents.JOIN.register((handler1, sender, client) -> {
            handler.handle(client);
        });
    }

    public static void onFovUpdate(FovUpdateHandler handler) {
        FOV_UPDATE.register(handler);
    }

    public static void onClientTicked(ClientTickedHandler handler) {
        ClientTickEvents.END_CLIENT_TICK.register(handler::handle);
    }

    public static void onScreenInitialized(ScreenInitializedHandler handler) {
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> handler.handle(screen));
    }

    public static void onClientStarted(ClientStartedHandler handler) {
        ClientLifecycleEvents.CLIENT_STARTED.register(handler::handle);
    }

    public static void onScreenDrawn(ScreenDrawnHandler handler) {
        screenDrawnHandlers.add(handler);
        initializeScreenEvents();
    }

    public static void onScreenKeyPressed(ScreenKeyPressedHandler handler) {
        screenKeyPressedHandlers.add(handler);
        initializeScreenEvents();
    }

    public static void onScreenMouseClick(ScreenMouseClickHandler handler) {
        screenMouseClickHandlers.add(handler);
        initializeScreenEvents();
    }

    public static void onScreenMouseRelease(ScreenMouseReleaseHandler handler) {
        screenMouseReleaseHandlers.add(handler);
        initializeScreenEvents();
    }

    private static void initializeScreenEvents() {
        if (beforeInitListener == null) {
            beforeInitListener = (client, screen, scaledWidth, scaledHeight) -> {
                for (ScreenDrawnHandler drawnHandler : screenDrawnHandlers) {
                    ScreenEvents.afterRender(screen).register((drawnScreen, matrices, mouseX, mouseY, tickDelta) -> drawnHandler.handle(drawnScreen, matrices, mouseX, mouseY));
                }

                for (ScreenKeyPressedHandler keyPressedHandler : screenKeyPressedHandlers) {
                    ScreenKeyboardEvents.afterKeyPress(screen).register(keyPressedHandler::handle);
                }

                for (ScreenMouseClickHandler mouseClickHandler : screenMouseClickHandlers) {
                    ScreenMouseEvents.beforeMouseClick(screen).register(mouseClickHandler::handle);
                }

                for (ScreenMouseReleaseHandler mouseReleaseHandler : screenMouseReleaseHandlers) {
                    ScreenMouseEvents.beforeMouseRelease(screen).register(mouseReleaseHandler::handle);
                }
            };
            ScreenEvents.BEFORE_INIT.register(beforeInitListener);
        }
    }

}
