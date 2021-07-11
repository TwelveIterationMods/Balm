package net.blay09.mods.balm.event;


import net.blay09.mods.balm.event.server.ServerStartedHandler;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

public class BalmEvents {

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

}
