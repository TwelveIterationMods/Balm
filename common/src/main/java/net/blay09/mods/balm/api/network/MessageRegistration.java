package net.blay09.mods.balm.api.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class MessageRegistration<TBuffer extends FriendlyByteBuf, TPayload extends CustomPacketPayload> {

    private final CustomPacketPayload.Type<TPayload> type;
    private final Class<TPayload> clazz;
    private final BiConsumer<TBuffer, TPayload> encodeFunc;
    private final Function<TBuffer, TPayload> decodeFunc;

    public MessageRegistration(CustomPacketPayload.Type<TPayload> type, Class<TPayload> clazz, BiConsumer<TBuffer, TPayload> encodeFunc, Function<TBuffer, TPayload> decodeFunc) {

        this.type = type;
        this.clazz = clazz;
        this.encodeFunc = encodeFunc;
        this.decodeFunc = decodeFunc;
    }

    public CustomPacketPayload.Type<TPayload> getType() {
        return type;
    }

    public Class<TPayload> getClazz() {
        return clazz;
    }

    public BiConsumer<TBuffer, TPayload> getEncodeFunc() {
        return encodeFunc;
    }

    public Function<TBuffer, TPayload> getDecodeFunc() {
        return decodeFunc;
    }

    public StreamCodec<TBuffer, TPayload> getCodec() {
        return StreamCodec.of((buffer, payload) -> getEncodeFunc().accept(buffer, payload), (buffer) -> getDecodeFunc().apply(buffer));
    }
}
