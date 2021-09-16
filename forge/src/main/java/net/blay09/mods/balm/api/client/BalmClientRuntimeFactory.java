package net.blay09.mods.balm.api.client;

import net.blay09.mods.balm.forge.client.ForgeBalmClientRuntime;

public class BalmClientRuntimeFactory {
    public static BalmClientRuntime create() {
        return new ForgeBalmClientRuntime();
    }
}
