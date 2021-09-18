package net.blay09.mods.balm.forge;

import net.blay09.mods.balm.forge.client.ForgeBalmClient;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("balm")
public class ForgeBalm {

    public ForgeBalm() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ForgeBalmClient::onInitializeClient);
    }

}
