package net.blay09.mods.balm.fabric.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.BalmConfig;
import net.blay09.mods.balm.api.event.ConfigReloadedEvent;
import net.minecraft.world.InteractionResult;

public class FabricBalmConfig implements BalmConfig {

    @Override
    @SuppressWarnings("unchecked")
    public <T> T initialize(Class<T> clazz) {
        Class<? extends ConfigData> configClazz = (Class<? extends ConfigData>) clazz;
        AutoConfig.register(configClazz, Toml4jConfigSerializer::new).registerSaveListener((configHolder, configData) -> {
            Balm.getEvents().fireEvent(new ConfigReloadedEvent());
            return InteractionResult.SUCCESS;
        });
        return (T) AutoConfig.getConfigHolder(configClazz).getConfig();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getConfig(Class<T> clazz) {
        Class<? extends ConfigData> configClazz = (Class<? extends ConfigData>) clazz;
        return (T) AutoConfig.getConfigHolder(configClazz).getConfig();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> void saveConfig(Class<T> clazz) {
        Class<? extends ConfigData> configClazz = (Class<? extends ConfigData>) clazz;
        AutoConfig.getConfigHolder(configClazz).save();
    }
}
