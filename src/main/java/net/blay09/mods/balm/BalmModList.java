package net.blay09.mods.balm;

import net.fabricmc.loader.api.FabricLoader;

public class BalmModList {
    public static boolean isLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }
}
