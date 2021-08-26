package net.blay09.mods.balm.api.config;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.ConfigReloadedEvent;
import net.blay09.mods.balm.api.event.PlayerLoginEvent;
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

public abstract class AbstractBalmConfig implements BalmConfig {

    private final AtomicReference<MinecraftServer> currentServer = new AtomicReference<>();
    private final Map<Class<?>, BalmConfigData> activeConfigs = new HashMap<>();
    private final Map<Class<?>, Function<?, ?>> syncMessageFactories = new HashMap<>();

    public void initialize() {
        Balm.getEvents().onEvent(ServerStartedEvent.class, event -> currentServer.set(event.getServer()));

        Balm.getEvents().onEvent(ServerStoppedEvent.class, event -> currentServer.set(null));

        Balm.getEvents().onEvent(PlayerLoginEvent.class, event -> {
            for (BalmConfigData config : activeConfigs.values()) {
                SyncConfigMessage<? extends BalmConfigData> message = getConfigSyncMessage(config.getClass());
                if (message != null) {
                    Balm.getNetworking().sendTo(event.getPlayer(), message);
                }
            }
        });

        Balm.getEvents().onEvent(ConfigReloadedEvent.class, event -> {
            if (currentServer.get() != null) {
                for (BalmConfigData config : activeConfigs.values()) {
                    SyncConfigMessage<? extends BalmConfigData> message = getConfigSyncMessage(config.getClass());
                    if (message != null) {
                        Balm.getNetworking().sendToAll(currentServer.get(), message);
                    }
                }
            }
        });
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends BalmConfigData> T getActive(Class<T> clazz) {
        return (T) activeConfigs.get(clazz);
    }

    @SuppressWarnings("unchecked")
    public <T extends BalmConfigData> SyncConfigMessage<T> getConfigSyncMessage(Class<T> clazz) {
        Function<BalmConfigData, SyncConfigMessage<BalmConfigData>> factory = getConfigSyncMessageFactory(clazz);
        return factory != null ? (SyncConfigMessage<T>) factory.apply(getBackingConfig(clazz)) : null;
    }

    @SuppressWarnings("unchecked")
    public <T extends BalmConfigData> Function<BalmConfigData, SyncConfigMessage<BalmConfigData>> getConfigSyncMessageFactory(Class<T> clazz) {
        return (Function<BalmConfigData, SyncConfigMessage<BalmConfigData>>) syncMessageFactories.get(clazz);
    }

    public <T extends BalmConfigData> void setActiveConfig(Class<T> clazz, T config) {
        activeConfigs.put(clazz, config);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends BalmConfigData> void handleSync(Player player, SyncConfigMessage<T> message) {
        T data = message.getData();
        setActiveConfig((Class<T>) data.getClass(), data);
    }

    @Override
    public <T extends BalmConfigData> void registerConfig(Class<T> clazz, Function<T, SyncConfigMessage<T>> syncMessageFactory) {
        setActiveConfig(clazz, Balm.getConfig().initializeBackingConfig(clazz));
        if (syncMessageFactory != null) {
            registerSyncMessageFactory(clazz, syncMessageFactory);
        }
    }

    private <T> void registerSyncMessageFactory(Class<T> clazz, Function<T, SyncConfigMessage<T>> syncMessageFactory) {
        syncMessageFactories.put(clazz, syncMessageFactory);
    }

    @Override
    public <T extends BalmConfigData> void updateConfig(Class<T> clazz, Consumer<T> consumer) {
        T fallback = getBackingConfig(clazz);
        consumer.accept(fallback);
        Balm.getConfig().saveBackingConfig(clazz);
    }
}