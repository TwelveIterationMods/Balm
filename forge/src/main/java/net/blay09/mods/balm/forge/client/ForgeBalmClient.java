package net.blay09.mods.balm.forge.client;

import net.blay09.mods.balm.forge.network.ForgeBalmNetworking;

public class ForgeBalmClient {
    public void onInitializeClient() {
        ForgeBalmNetworking.initializeClientHandlers();
    }
}
