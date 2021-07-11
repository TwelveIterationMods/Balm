package net.blay09.mods.balm.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.blay09.mods.balm.event.BalmEvents;
import net.minecraft.world.InteractionResult;

public class BalmConfig implements ConfigData {

    public static <T extends ConfigData> T initialize(Class<T> clazz) {
        AutoConfig.register(clazz, Toml4jConfigSerializer::new).registerSaveListener((configHolder, configData) -> {
            BalmEvents.CONFIG_RELOADED.invoker().handle();
            return InteractionResult.SUCCESS;
        });
        return AutoConfig.getConfigHolder(clazz).getConfig();
    }

    public static <T extends BalmConfig> T getConfig(Class<T> clazz) {
        return AutoConfig.getConfigHolder(clazz).getConfig();
    }

    public static <T extends BalmConfig> void saveConfig(Class<T> clazz) {
        AutoConfig.getConfigHolder(clazz).save();
    }
}
