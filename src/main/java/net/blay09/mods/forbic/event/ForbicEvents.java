package net.blay09.mods.forbic.event;


import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenKeyboardEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

import java.util.ArrayList;
import java.util.List;

public class ForbicEvents {

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

    public static Event<LivingDamageHandler> LIVING_DAMAGE = EventFactory.createArrayBacked(LivingDamageHandler.class,
            (listeners) -> (entity) -> {
                for (LivingDamageHandler listener : listeners) {
                    listener.handle(entity);
                }
            });

    public static Event<ConfigReloadedHandler> CONFIG_RELOADED = EventFactory.createArrayBacked(ConfigReloadedHandler.class,
            (listeners) -> () -> {
                for (ConfigReloadedHandler listener : listeners) {
                    listener.handle();
                }
            });

    public static Event<ItemCraftedHandler> ITEM_CRAFTED = EventFactory.createArrayBacked(ItemCraftedHandler.class,
            (listeners) -> (player, itemStack, craftMatrix) -> {
                for (ItemCraftedHandler listener : listeners) {
                    listener.handle(player, itemStack, craftMatrix);
                }
            });

    public static void onPlayerLogin(PlayerLoginHandler handler) {
        ServerPlayConnectionEvents.JOIN.register((listener, sender, server) -> handler.handle(listener.player));
    }

    public static void onFovUpdate(FovUpdateHandler handler) {
        FOV_UPDATE.register(handler);
    }

    public static void onLivingDamage(LivingDamageHandler handler) {
        LIVING_DAMAGE.register(handler);
    }

    public static void onConfigReloaded(ConfigReloadedHandler handler) {
        CONFIG_RELOADED.register(handler);
    }

    public static void onItemCrafted(ItemCraftedHandler handler) {
        ITEM_CRAFTED.register(handler);
    }

    public static void onServerStarted(ServerStartedHandler handler) {
        ServerLifecycleEvents.SERVER_STARTED.register(handler::handle);
    }

    public static void onServerStopped(ServerStartedHandler handler) {
        ServerLifecycleEvents.SERVER_STOPPED.register(handler::handle);
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
