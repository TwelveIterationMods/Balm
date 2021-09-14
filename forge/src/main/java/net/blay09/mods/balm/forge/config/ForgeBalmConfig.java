package net.blay09.mods.balm.forge.config;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.AbstractBalmConfig;
import net.blay09.mods.balm.api.config.BalmConfigData;
import net.blay09.mods.balm.api.event.ConfigReloadedEvent;
import net.minecraft.world.InteractionResult;

public class ForgeBalmConfig extends AbstractBalmConfig {

    @Override
    public <T extends BalmConfigData> T initializeBackingConfig(Class<T> clazz) {
        AutoConfig.register(clazz, Toml4jConfigSerializer::new).registerSaveListener((configHolder, configData) -> {
            Balm.getEvents().fireEvent(new ConfigReloadedEvent());
            return InteractionResult.SUCCESS;
        });
        return (T) AutoConfig.getConfigHolder(clazz).getConfig();
    }

    @Override
    public <T extends BalmConfigData> T getBackingConfig(Class<T> clazz) {
        return (T) AutoConfig.getConfigHolder(clazz).getConfig();
    }

    @Override
    public <T extends BalmConfigData> void saveBackingConfig(Class<T> clazz) {
        AutoConfig.getConfigHolder(clazz).save();
    }
}
