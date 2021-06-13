package net.blay09.mods.forbic.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;

public class ForbicConfig implements ConfigData {

    public static <T extends ForbicConfig> T getConfig(Class<T> clazz) {
        return AutoConfig.getConfigHolder(clazz).getConfig();
    }

}
