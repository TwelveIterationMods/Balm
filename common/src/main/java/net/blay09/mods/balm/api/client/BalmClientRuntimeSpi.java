package net.blay09.mods.balm.api.client;

import net.blay09.mods.balm.api.BalmRuntimeLoadContext;

import java.util.ServiceLoader;

public class BalmClientRuntimeSpi {
    @SuppressWarnings("unchecked")
    public static BalmClientRuntime<BalmRuntimeLoadContext> create() {
        var loader = ServiceLoader.load(BalmClientRuntimeFactory.class);
        var factory = loader.findFirst().orElseThrow();
        return (BalmClientRuntime<BalmRuntimeLoadContext>) factory.create();
    }
}
