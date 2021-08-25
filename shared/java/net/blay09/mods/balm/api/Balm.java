package net.blay09.mods.balm.api;

import net.blay09.mods.balm.api.config.BalmConfig;
import net.blay09.mods.balm.api.event.BalmEvents;
import net.blay09.mods.balm.api.network.BalmNetworking;

public class Balm {
    private static BalmRuntime runtime;

    public static void __setupRuntime(BalmRuntime runtime) {
        Balm.runtime = runtime;
    }

    public static boolean isModLoaded(String modId) {
        return runtime.isModLoaded(modId);
    }

    public static BalmEvents getEvents() {
        return runtime.getEvents();
    }

    public static BalmConfig getConfig() {
        return runtime.getConfig();
    }

    public static BalmNetworking getNetworking() {
        return runtime.getNetworking();
    }
}
