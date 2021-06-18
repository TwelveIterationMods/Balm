package net.blay09.mods.forbic.event;


import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

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
    ;

    public static void onPlayerLogin(PlayerLoginHandler handler) {
        ServerPlayConnectionEvents.JOIN.register((listener, sender, server) -> handler.handle(listener.player));
    }

    public static void onFovUpdate(FovUpdateHandler handler) {
        FOV_UPDATE.register(handler);
    }

    public static void onLivingDamage(LivingDamageHandler handler) {
        // TODO
    }

    public static void onConfigReloaded(ConfigReloadedHandler handler) {
        // TODO
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
