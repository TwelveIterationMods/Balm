package net.blay09.mods.forbic;

import net.fabricmc.loader.api.FabricLoader;

public class ForbicModList {
    public static boolean isLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }
}
