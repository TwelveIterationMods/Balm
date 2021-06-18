package net.blay09.mods.forbic.event;


import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.List;

public class ForbicEvents {

    private static final List<ScreenDrawnHandler> screenDrawnHandlers = new ArrayList<>();
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

    public static void onServerStarted(ServerStartedHandler handler) {
        ServerLifecycleEvents.SERVER_STARTED.register(handler::handle);
    }

    public static void onServerStopped(ServerStartedHandler handler) {
        ServerLifecycleEvents.SERVER_STOPPED.register(handler::handle);
    }

    public static void onScreenInitialized(ScreenInitializedHandler handler) {
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> handler.handle(screen));
    }

    public static void onScreenDrawn(ScreenDrawnHandler handler) {
        screenDrawnHandlers.add(handler);

        if (beforeInitListener == null) {
            beforeInitListener = (client, screen, scaledWidth, scaledHeight) -> {
                for (ScreenDrawnHandler drawnHandler : screenDrawnHandlers) {
                    ScreenEvents.afterRender(screen).register((drawnScreen, matrices, mouseX, mouseY, tickDelta) -> drawnHandler.handle(drawnScreen, matrices, mouseX, mouseY));
                }
            };
            ScreenEvents.BEFORE_INIT.register(beforeInitListener);
        }
    }

}
