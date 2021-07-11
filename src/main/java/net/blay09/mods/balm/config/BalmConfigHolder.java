package net.blay09.mods.balm.config;

import net.blay09.mods.balm.event.BalmEvents;
import net.blay09.mods.balm.network.BalmNetworking;
import net.blay09.mods.balm.network.SyncConfigMessage;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;

public class BalmConfigHolder<T extends BalmConfig> {

    private static final AtomicReference<MinecraftServer> currentServer = new AtomicReference<>();
    private static final Map<Class<?>, BalmConfig> activeConfigs = new HashMap<>();
    private static final Map<Class<?>, Function<?, ?>> syncMessageFactories = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static <T extends BalmConfig> T getActive(Class<T> clazz) {
        return (T) activeConfigs.get(clazz);
    }

    public static <T extends BalmConfig> T getFallback(Class<T> clazz) {
        return BalmConfig.getConfig(clazz);
    }

    @SuppressWarnings("unchecked")
    public static <T extends BalmConfig> SyncConfigMessage<T> getConfigSyncMessage(Class<T> clazz) {
        Function<BalmConfig, SyncConfigMessage<BalmConfig>> factory = getConfigSyncMessageFactory(clazz);
        return (SyncConfigMessage<T>) factory.apply(getFallback(clazz));
    }

    @SuppressWarnings("unchecked")
    public static <T extends BalmConfig> Function<BalmConfig, SyncConfigMessage<BalmConfig>> getConfigSyncMessageFactory(Class<T> clazz) {
        return (Function<BalmConfig, SyncConfigMessage<BalmConfig>>) syncMessageFactories.get(clazz);
    }

    public static <T extends BalmConfig> void setActiveConfig(Class<T> clazz, T config) {
        activeConfigs.put(clazz, config);
    }

    @SuppressWarnings("unchecked")
    public static <T extends BalmConfig> void handleSync(Player player, SyncConfigMessage<T> message) {
        T data = message.getData();
        setActiveConfig((Class<T>) data.getClass(), data);
    }

    public static <T extends BalmConfig> void registerConfig(Class<T> clazz, Function<T, SyncConfigMessage<T>> syncMessageFactory) {
        setActiveConfig(clazz, BalmConfig.initialize(clazz));
        registerSyncMessageFactory(clazz, syncMessageFactory);
    }

    private static <T> void registerSyncMessageFactory(Class<T> clazz, Function<T, SyncConfigMessage<T>> syncMessageFactory) {
        syncMessageFactories.put(clazz, syncMessageFactory);
    }

    public static void initialize() {
        BalmEvents.onServerStarted(currentServer::set);
        BalmEvents.onServerStopped(server -> {
            currentServer.set(null);
        });

        BalmEvents.onConfigReloaded(() -> {
            if (currentServer.get() != null) {
                for (BalmConfig config : activeConfigs.values()) {
                    BalmNetworking.sendToAll(currentServer.get(), getConfigSyncMessage(config.getClass()));
                }
            }
        });
    }

    public static <T extends BalmConfig> void updateConfig(Class<T> clazz, Consumer<T> consumer) {
        T fallback = getFallback(clazz);
        consumer.accept(fallback);
        BalmConfig.saveConfig(clazz);
    }
}