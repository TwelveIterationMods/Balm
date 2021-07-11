package net.blay09.mods.balm;

import net.blay09.mods.balm.api.IBalmPlayer;
import net.blay09.mods.balm.config.BalmConfigHolder;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.nbt.CompoundTag;

public class Balm implements ModInitializer {
    @Override
    public void onInitialize() {
        ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) -> {
            CompoundTag data = ((IBalmPlayer) oldPlayer).getBalmData();
            ((IBalmPlayer) newPlayer).setBalmData(data);
        });

        BalmConfigHolder.initialize();
    }
}
