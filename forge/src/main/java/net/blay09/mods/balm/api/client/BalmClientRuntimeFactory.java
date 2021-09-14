package net.blay09.mods.balm.api.client;

public class BalmClientRuntimeFactory {
    public static BalmClientRuntime create() {
        return new ForgeBalmClientRuntime();
    }
}
