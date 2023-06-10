package net.blay09.mods.balm.api.config;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.ConfigReloadedEvent;
import net.blay09.mods.balm.api.event.PlayerLoginEvent;
import net.blay09.mods.balm.api.network.SyncConfigMessage;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class AbstractBalmConfig implements BalmConfig {

    private final Map<Class<?>, BalmConfigData> activeConfigs = new HashMap<>();
    private final Map<Class<?>, Function<?, ?>> syncMessageFactories = new HashMap<>();

    public void initialize() {
        Balm.getEvents().onEvent(PlayerLoginEvent.class, event -> {
            for (BalmConfigData config : activeConfigs.values()) {
                SyncConfigMessage<? extends BalmConfigData> message = getConfigSyncMessage(config.getClass());
                if (message != null) {
                    Balm.getNetworking().sendTo(event.getPlayer(), message);
                }
            }
        });

        Balm.getEvents().onEvent(ConfigReloadedEvent.class, event -> {
            MinecraftServer server = Balm.getHooks().getServer();
            if (server != null) {
                for (BalmConfigData config : activeConfigs.values()) {
                    SyncConfigMessage<? extends BalmConfigData> message = getConfigSyncMessage(config.getClass());
                    if (message != null) {
                        Balm.getNetworking().sendToAll(server, message);
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
        T backingConfig = getBackingConfig(clazz);
        consumer.accept(backingConfig);
        Balm.getConfig().saveBackingConfig(clazz);

        // If active config does not match backing config, apply changes to the active config as well
        // This assumes that the client-side does not use updateConfig to change server-side configs, as that would result in a desync
        T activeConfig = getActive(clazz);
        if (activeConfig != backingConfig) {
            consumer.accept(getActive(clazz));
        }
    }

    @Override
    public <T extends BalmConfigData> void resetToBackingConfig(Class<T> clazz) {
        setActiveConfig(clazz, getBackingConfig(clazz));
    }

    @Override
    @SuppressWarnings("unchecked")
    public void resetToBackingConfigs() {
        for (Class<?> clazz : activeConfigs.keySet()) {
            resetToBackingConfig((Class<? extends BalmConfigData>) clazz);
        }
    }

    @NotNull
    protected <T> T createConfigDataInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();

        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException("Config class or sub-class missing a public no-arg constructor.", e);
        }
    }
}