package net.blay09.mods.forbic.network;

import net.blay09.mods.forbic.client.ForbicClient;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class ForbicNetworking {

    private static final Map<Class<?>, ForbicMessageRegistration<?>> messagesByClass = new HashMap<>();
    private static final Map<ResourceLocation, ForbicMessageRegistration<?>> messagesByIdentifier = new HashMap<>();

    private static final List<ForbicClientboundMessageRegistration<?>> clientMessageRegistrations = new ArrayList<>();

    public static void openGui(Player player, MenuProvider menuProvider) {
        player.openMenu(menuProvider);
    }

    @SuppressWarnings("unchecked")
    public static <T> void sendTo(Player player, T message) {
        ForbicMessageRegistration<T> messageRegistration = (ForbicMessageRegistration<T>) messagesByClass.get(message.getClass());
        ResourceLocation identifier = messageRegistration.getIdentifier();
        FriendlyByteBuf buf = PacketByteBufs.create();
        messageRegistration.getEncodeFunc().accept(message, buf);
        ServerPlayNetworking.send((ServerPlayer) player, identifier, buf);
    }

    @SuppressWarnings("unchecked")
    public static <T> void sendToTracking(ServerLevel world, BlockPos pos, T message) {
        ForbicMessageRegistration<T> messageRegistration = (ForbicMessageRegistration<T>) messagesByClass.get(message.getClass());
        ResourceLocation identifier = messageRegistration.getIdentifier();
        FriendlyByteBuf buf = PacketByteBufs.create();
        messageRegistration.getEncodeFunc().accept(message, buf);
        for (ServerPlayer player : PlayerLookup.tracking(world, pos)) {
            ServerPlayNetworking.send(player, identifier, buf);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> void sendToAll(MinecraftServer server, T message) {
        ForbicMessageRegistration<T> messageRegistration = (ForbicMessageRegistration<T>) messagesByClass.get(message.getClass());
        ResourceLocation identifier = messageRegistration.getIdentifier();
        FriendlyByteBuf buf = PacketByteBufs.create();
        messageRegistration.getEncodeFunc().accept(message, buf);
        for (ServerPlayer player : PlayerLookup.all(server)) {
            ServerPlayNetworking.send(player, identifier, buf);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> void sendToServer(T message) {
        ForbicMessageRegistration<T> messageRegistration = (ForbicMessageRegistration<T>) messagesByClass.get(message.getClass());
        ResourceLocation identifier = messageRegistration.getIdentifier();
        FriendlyByteBuf buf = PacketByteBufs.create();
        messageRegistration.getEncodeFunc().accept(message, buf);
        ClientPlayNetworking.send(identifier, buf);
    }

    public static <T> void registerClientboundPacket(ResourceLocation identifier, Class<T> clazz, BiConsumer<T, FriendlyByteBuf> encodeFunc, Function<FriendlyByteBuf, T> decodeFunc, BiConsumer<Player, T> handler) {
        ForbicClientboundMessageRegistration<T> messageRegistration = new ForbicClientboundMessageRegistration<>(identifier, clazz, encodeFunc, decodeFunc, handler);

        messagesByClass.put(clazz, messageRegistration);
        messagesByIdentifier.put(identifier, messageRegistration);

        clientMessageRegistrations.add(messageRegistration);
    }

    public static <T> void registerServerboundPacket(ResourceLocation identifier, Class<T> clazz, BiConsumer<T, FriendlyByteBuf> encodeFunc, Function<FriendlyByteBuf, T> decodeFunc, BiConsumer<ServerPlayer, T> handler) {
        ForbicMessageRegistration<T> messageRegistration = new ForbicServerboundMessageRegistration<>(identifier, clazz, encodeFunc, decodeFunc, handler);

        messagesByClass.put(clazz, messageRegistration);
        messagesByIdentifier.put(identifier, messageRegistration);
        ServerPlayNetworking.registerGlobalReceiver(identifier, ((server, player, listener, buf, responseSender) -> {
            T message = messageRegistration.getDecodeFunc().apply(buf);
            server.execute(() -> handler.accept(player, message));
        }));
    }

    public static void initializeClientHandlers() {
        for (ForbicClientboundMessageRegistration<?> message : clientMessageRegistrations) {
            registerClientHandler(message);
        }
    }

    private static <T> void registerClientHandler(ForbicClientboundMessageRegistration<T> messageRegistration) {
        ResourceLocation identifier = messageRegistration.getIdentifier();
        BiConsumer<Player, T> handler = messageRegistration.getHandler();
        ClientPlayNetworking.registerGlobalReceiver(identifier, ((client, listener, buf, responseSender) -> {
            T message = messageRegistration.getDecodeFunc().apply(buf);
            client.execute(() -> handler.accept(ForbicClient.getClientPlayer(), message));
        }));
    }
}
