package net.blay09.mods.forbic.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.blay09.mods.forbic.event.ForbicEvents;
import net.minecraft.world.InteractionResult;

public class ForbicConfig implements ConfigData {

    public static <T extends ConfigData> T initialize(Class<T> clazz) {
        AutoConfig.register(clazz, Toml4jConfigSerializer::new).registerSaveListener((configHolder, configData) -> {
            ForbicEvents.CONFIG_RELOADED.invoker().handle();
            return InteractionResult.SUCCESS;
        });
        return AutoConfig.getConfigHolder(clazz).getConfig();
    }

    public static <T extends ForbicConfig> T getConfig(Class<T> clazz) {
        return AutoConfig.getConfigHolder(clazz).getConfig();
    }

    public static <T extends ForbicConfig> void saveConfig(Class<T> clazz) {
        AutoConfig.getConfigHolder(clazz).save();
    }
}
