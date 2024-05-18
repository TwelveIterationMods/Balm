package net.blay09.mods.balm.neoforge.client;

import net.blay09.mods.balm.api.client.BalmClientRuntime;
import net.blay09.mods.balm.api.client.BalmClientRuntimeFactory;

public class NeoForgeBalmClientRuntimeFactory implements BalmClientRuntimeFactory {
    @Override
    public BalmClientRuntime<?> create() {
        return new NeoForgeBalmClientRuntime();
    }
}
