package net.blay09.mods.balm.fabric;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.BalmConfigHolder;
import net.blay09.mods.balm.api.entity.BalmPlayer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.nbt.CompoundTag;

public class FabricBalmMod implements ModInitializer {

    public FabricBalmMod() {
        Balm.__setupRuntime(new FabricBalmRuntime());
    }

    @Override
    public void onInitialize() {
        ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) -> {
            CompoundTag data = ((BalmPlayer) oldPlayer).getBalmData();
            ((BalmPlayer) newPlayer).setBalmData(data);
        });

        BalmConfigHolder.initialize();
    }
}
