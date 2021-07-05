package net.blay09.mods.forbic;

import net.blay09.mods.forbic.api.IForbicPlayer;
import net.blay09.mods.forbic.mixin.PlayerMixin;
import net.blay09.mods.forbic.network.ForbicClientNetworking;
import net.blay09.mods.forbic.network.ForbicNetworking;
import net.blay09.mods.forbic.network.SyncConfigMessage;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public class Forbic implements ModInitializer {
    @Override
    public void onInitialize() {
        ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) -> {
            CompoundTag forbicData = ((IForbicPlayer) oldPlayer).getForbicData();
            ((IForbicPlayer) newPlayer).setForbicData(forbicData);
        });

        ForbicNetworking.registerClientboundPacket(new ResourceLocation("test", "asd"), SyncConfigMessage.class, (message, friendlyByteBuf) -> {
        }, it -> null, (a, b) -> {
        });
    }
}
