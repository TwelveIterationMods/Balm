package net.blay09.mods.balm.fabric.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.AbstractBalmConfig;
import net.blay09.mods.balm.api.config.BalmConfigData;
import net.blay09.mods.balm.api.event.ConfigReloadedEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionResult;

import java.io.File;

public class FabricBalmConfig extends AbstractBalmConfig {

    @Override
    @SuppressWarnings("unchecked")
    public <T extends BalmConfigData> T initializeBackingConfig(Class<T> clazz) {
        var configDataClass = (Class<? extends ConfigData>) clazz;
        AutoConfig.register(configDataClass, Toml4jConfigSerializer::new).registerSaveListener((configHolder, configData) -> {
            Balm.getEvents().fireEvent(new ConfigReloadedEvent());
            return InteractionResult.SUCCESS;
        });
        return (T) AutoConfig.getConfigHolder(configDataClass).getConfig();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends BalmConfigData> T getBackingConfig(Class<T> clazz) {
        var configDataClass = (Class<? extends ConfigData>) clazz;
        return (T) AutoConfig.getConfigHolder(configDataClass).getConfig();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends BalmConfigData> void saveBackingConfig(Class<T> clazz) {
        var configDataClass = (Class<? extends ConfigData>) clazz;
        AutoConfig.getConfigHolder(configDataClass).save();
    }

    @Override
    public File getConfigDir() {
        return new File(Minecraft.getInstance().gameDirectory, "config");
    }
}
