package net.blay09.mods.balm.event;


import net.blay09.mods.balm.event.server.ServerStartedHandler;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;

public class BalmEvents {

    private static final List<PlayerTickHandler> playerTickHandlers = new ArrayList<>();
    private static ServerTickEvents.StartTick serverTickListener = null;

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

    public static Event<PlayerOpenMenuHandler> OPEN_MENU = EventFactory.createArrayBacked(PlayerOpenMenuHandler.class,
            (listeners) -> (player, menu) -> {
                for (PlayerOpenMenuHandler listener : listeners) {
                    listener.handle(player, menu);
                }
            });

    public static void onPlayerLogin(PlayerLoginHandler handler) {
        ServerPlayConnectionEvents.JOIN.register((listener, sender, server) -> handler.handle(listener.player));
    }

    public static void onPlayerRespawn(PlayerRespawnHandler handler) {
        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> handler.handle(oldPlayer, newPlayer));
    }

    public static void onPlayerOpenMenu(PlayerOpenMenuHandler handler) {
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

    public static void onUseBlock(UseBlockHandler handler) {
        UseBlockCallback.EVENT.register(handler::handle);
    }

    public static void onServerStarted(ServerStartedHandler handler) {
        ServerLifecycleEvents.SERVER_STARTED.register(handler::handle);
    }

    public static void onServerStopped(ServerStartedHandler handler) {
        ServerLifecycleEvents.SERVER_STOPPED.register(handler::handle);
    }

    public static void onBlockBroken(BlockBrokenHandler handler) {
        PlayerBlockBreakEvents.AFTER.register(handler::handle);
    }

    public static void onPlayerTick(PlayerTickHandler handler) {
        if (serverTickListener == null) {
            serverTickListener = server -> {
                for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                    for (PlayerTickHandler playerTickHandler : playerTickHandlers) {
                        playerTickHandler.handle(player);
                    }
                }
            };

            ServerTickEvents.START_SERVER_TICK.register(serverTickListener);
        }

        playerTickHandlers.add(handler);
    }

}
