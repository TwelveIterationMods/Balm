package net.blay09.mods.balm.api.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class ClientboundMessageRegistration<TBuffer extends FriendlyByteBuf, TPayload extends CustomPacketPayload> extends MessageRegistration<TBuffer, TPayload> {

    private final BiConsumer<Player, TPayload> handler;

    public ClientboundMessageRegistration(CustomPacketPayload.Type<TPayload> type, Class<TPayload> clazz, BiConsumer<TBuffer, TPayload> encodeFunc, Function<TBuffer, TPayload> decodeFunc, BiConsumer<Player, TPayload> handler) {
        super(type, clazz, encodeFunc, decodeFunc);
        this.handler = handler;
    }

    public BiConsumer<Player, TPayload> getHandler() {
        return handler;
    }
}
