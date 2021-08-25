package net.blay09.mods.balm.api.config;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.ConfigReloadedEvent;
import net.blay09.mods.balm.api.event.server.ServerStartedEvent;
import net.blay09.mods.balm.api.event.server.ServerStoppedEvent;
import net.blay09.mods.balm.api.network.SyncConfigMessage;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;

public class BalmConfigHolder {

    private static final AtomicReference<MinecraftServer> currentServer = new AtomicReference<>();
    private static final Map<Class<?>, BalmConfig> activeConfigs = new HashMap<>();
    private static final Map<Class<?>, Function<?, ?>> syncMessageFactories = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static <T extends BalmConfig> T getActive(Class<T> clazz) {
        return (T) activeConfigs.get(clazz);
    }

    public static <T extends BalmConfig> T getFallback(Class<T> clazz) {
        return Balm.getConfig().getConfig(clazz);
    }

    @SuppressWarnings("unchecked")
    public static <T extends BalmConfig> SyncConfigMessage<T> getConfigSyncMessage(Class<T> clazz) {
        Function<BalmConfig, SyncConfigMessage<BalmConfig>> factory = getConfigSyncMessageFactory(clazz);
        return factory != null ? (SyncConfigMessage<T>) factory.apply(getFallback(clazz)) : null;
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
        setActiveConfig(clazz, Balm.getConfig().initialize(clazz));
        if (syncMessageFactory != null) {
            registerSyncMessageFactory(clazz, syncMessageFactory);
        }
    }

    private static <T> void registerSyncMessageFactory(Class<T> clazz, Function<T, SyncConfigMessage<T>> syncMessageFactory) {
        syncMessageFactories.put(clazz, syncMessageFactory);
    }

    public static void initialize() {
        Balm.getEvents().onEvent(ServerStartedEvent.class, event -> currentServer.set(event.getServer()));

        Balm.getEvents().onEvent(ServerStoppedEvent.class, event -> currentServer.set(null));

        Balm.getEvents().onEvent(ConfigReloadedEvent.class, event -> {
            if (currentServer.get() != null) {
                for (BalmConfig config : activeConfigs.values()) {
                    SyncConfigMessage<? extends BalmConfig> message = getConfigSyncMessage(config.getClass());
                    if (message != null) {
                        Balm.getNetworking().sendToAll(currentServer.get(), message);
                    }
                }
            }
        });
    }

    public static <T extends BalmConfig> void updateConfig(Class<T> clazz, Consumer<T> consumer) {
        T fallback = getFallback(clazz);
        consumer.accept(fallback);
        Balm.getConfig().saveConfig(clazz);
    }
}