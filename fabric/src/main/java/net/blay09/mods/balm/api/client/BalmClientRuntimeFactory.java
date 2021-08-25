package net.blay09.mods.balm.api.client;

import net.blay09.mods.balm.fabric.client.FabricBalmClientRuntime;

public class BalmClientRuntimeFactory {
    public static BalmClientRuntime create() {
        return new FabricBalmClientRuntime();
    }
}
