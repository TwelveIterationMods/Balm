package net.blay09.mods.forbic.config;

import net.blay09.mods.forbic.event.ForbicEvents;
import net.blay09.mods.forbic.network.ForbicNetworking;
import net.blay09.mods.forbic.network.SyncConfigMessage;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

public class ForbicConfigHolder<T extends ForbicConfig> {

    private static final AtomicReference<MinecraftServer> currentServer = new AtomicReference<>();
    private static final Map<Class<?>, ForbicConfig> activeConfigs = new HashMap<>();
    private static final Map<Class<?>, Function<ForbicConfig, SyncConfigMessage<ForbicConfig>>> syncMessageFactories = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static <T extends ForbicConfig> T getActive(Class<T> clazz) {
        return (T) activeConfigs.get(clazz);
    }

    public static <T extends ForbicConfig> T getFallback(Class<T> clazz) {
        return ForbicConfig.getConfig(clazz);
    }

    @SuppressWarnings("unchecked")
    public static <T extends ForbicConfig> SyncConfigMessage<T> getConfigSyncMessage(Class<T> clazz) {
        Function<ForbicConfig, SyncConfigMessage<ForbicConfig>> factory = getConfigSyncMessageFactory(clazz);
        return (SyncConfigMessage<T>) factory.apply(getFallback(clazz));
    }

    public static <T extends ForbicConfig> Function<ForbicConfig, SyncConfigMessage<ForbicConfig>> getConfigSyncMessageFactory(Class<T> clazz) {
        return syncMessageFactories.get(clazz);
    }

    public static <T extends ForbicConfig> void setActiveConfig(Class<T> clazz, T config) {
        activeConfigs.put(clazz, config);
    }

    @SuppressWarnings("unchecked")
    public static <T extends ForbicConfig> void handleSync(Player player, SyncConfigMessage<T> message) {
        T data = message.getData();
        setActiveConfig((Class<T>) data.getClass(), data);
    }

    public static <T extends ForbicConfig> void registerConfig(Class<T> clazz, Function<ForbicConfig, SyncConfigMessage<ForbicConfig>> syncMessageFactory) {
        setActiveConfig(clazz, ForbicConfig.initialize(clazz));
        registerSyncMessageFactory(clazz, syncMessageFactory);
    }

    private static void registerSyncMessageFactory(Class<?> clazz, Function<ForbicConfig, SyncConfigMessage<ForbicConfig>> syncMessageFactory) {
        syncMessageFactories.put(clazz, syncMessageFactory);
    }

    public static void initialize() {
        ForbicEvents.onServerStarted(currentServer::set);
        ForbicEvents.onServerStopped(server -> {
            currentServer.set(null);
        });

        ForbicEvents.onConfigReloaded(() -> {
            if (currentServer.get() != null) {
                for (ForbicConfig config : activeConfigs.values()) {
                    ForbicNetworking.sendToAll(currentServer.get(), getConfigSyncMessage(config.getClass()));
                }
            }
        });
    }

}