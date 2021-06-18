package net.blay09.mods.forbic;

import net.blay09.mods.forbic.mixin.PlayerMixin;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.nbt.CompoundTag;

public class Forbic implements ModInitializer {
    @Override
    public void onInitialize() {
        ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) -> {
            CompoundTag forbicData = ((PlayerMixin) (Object) oldPlayer).getForbicData();
            ((PlayerMixin) (Object) newPlayer).setForbicData(forbicData);
        });
    }
}
