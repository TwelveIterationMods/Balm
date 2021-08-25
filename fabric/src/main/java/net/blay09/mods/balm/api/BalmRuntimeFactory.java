package net.blay09.mods.balm.api;

import net.blay09.mods.balm.fabric.FabricBalmRuntime;

public class BalmRuntimeFactory {
    public static BalmRuntime create() {
        return new FabricBalmRuntime();
    }
}
