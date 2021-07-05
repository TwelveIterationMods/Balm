package net.blay09.mods.forbic.network;

import net.blay09.mods.forbic.client.ForbicClient;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.function.BiConsumer;

public class ForbicClientNetworking {
    public static <T> void registerClientGlobalReceiver(ResourceLocation identifier, BiConsumer<Player, T> handler, ForbicMessageRegistration<T> messageRegistration) {
        ClientPlayNetworking.registerGlobalReceiver(identifier, ((client, listener, buf, responseSender) -> {
            T message = messageRegistration.getDecodeFunc().apply(buf);
            client.execute(() -> handler.accept(ForbicClient.getClientPlayer(), message));
        }));
    }
}
