package net.blay09.mods.balm.forge.client;

import net.blay09.mods.balm.forge.network.ForgeBalmNetworking;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ForgeBalmClient {
    public static void onInitializeClient(FMLClientSetupEvent event) {
        ForgeBalmNetworking.initializeClientHandlers();
    }
}
