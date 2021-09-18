package net.blay09.mods.balm.forge.network;

import net.blay09.mods.balm.api.client.BalmClient;
import net.blay09.mods.balm.api.network.BalmNetworking;
import net.blay09.mods.balm.api.network.ClientboundMessageRegistration;
import net.blay09.mods.balm.api.network.MessageRegistration;
import net.blay09.mods.balm.api.network.ServerboundMessageRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fmllegacy.network.NetworkEvent;
import net.minecraftforge.fmllegacy.network.PacketDistributor;
import net.minecraftforge.fmllegacy.network.simple.SimpleChannel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class ForgeBalmNetworking implements BalmNetworking {

    private static final Map<Class<?>, MessageRegistration<?>> messagesByClass = new HashMap<>();
    private static final Map<ResourceLocation, MessageRegistration<?>> messagesByIdentifier = new HashMap<>();

    private static final List<ClientboundMessageRegistration<?>> clientMessageRegistrations = new ArrayList<>();

    @Override
    public void openGui(Player player, MenuProvider menuProvider) {
        player.openMenu(menuProvider);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> void sendTo(Player player, T message) {
        MessageRegistration<T> messageRegistration = (MessageRegistration<T>) messagesByClass.get(message.getClass());
        ResourceLocation identifier = messageRegistration.getIdentifier();

        SimpleChannel channel = NetworkChannels.get(identifier.getNamespace());
        channel.send(PacketDistributor.PLAYER.with(() -> ((ServerPlayer) player)), message);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> void sendToTracking(ServerLevel world, BlockPos pos, T message) {
        MessageRegistration<T> messageRegistration = (MessageRegistration<T>) messagesByClass.get(message.getClass());
        ResourceLocation identifier = messageRegistration.getIdentifier();

        SimpleChannel channel = NetworkChannels.get(identifier.getNamespace());
        channel.send(PacketDistributor.TRACKING_CHUNK.with(() -> world.getChunkAt(pos)), message);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> void sendToAll(MinecraftServer server, T message) {
        MessageRegistration<T> messageRegistration = (MessageRegistration<T>) messagesByClass.get(message.getClass());
        ResourceLocation identifier = messageRegistration.getIdentifier();

        SimpleChannel channel = NetworkChannels.get(identifier.getNamespace());
        channel.send(PacketDistributor.ALL.noArg(), message);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> void sendToServer(T message) {
        MessageRegistration<T> messageRegistration = (MessageRegistration<T>) messagesByClass.get(message.getClass());
        ResourceLocation identifier = messageRegistration.getIdentifier();

        SimpleChannel channel = NetworkChannels.get(identifier.getNamespace());
        channel.sendToServer(message);
    }

    @Override
    public <T> void registerClientboundPacket(ResourceLocation identifier, Class<T> clazz, BiConsumer<T, FriendlyByteBuf> encodeFunc, Function<FriendlyByteBuf, T> decodeFunc, BiConsumer<Player, T> handler) {
        ClientboundMessageRegistration<T> messageRegistration = new ClientboundMessageRegistration<>(identifier, clazz, encodeFunc, decodeFunc, handler);

        messagesByClass.put(clazz, messageRegistration);
        messagesByIdentifier.put(identifier, messageRegistration);

        clientMessageRegistrations.add(messageRegistration);
    }

    @Override
    public <T> void registerServerboundPacket(ResourceLocation identifier, Class<T> clazz, BiConsumer<T, FriendlyByteBuf> encodeFunc, Function<FriendlyByteBuf, T> decodeFunc, BiConsumer<ServerPlayer, T> handler) {
        MessageRegistration<T> messageRegistration = new ServerboundMessageRegistration<>(identifier, clazz, encodeFunc, decodeFunc, handler);

        messagesByClass.put(clazz, messageRegistration);
        messagesByIdentifier.put(identifier, messageRegistration);

        SimpleChannel channel = NetworkChannels.get(identifier.getNamespace());
        channel.registerMessage(discriminator(identifier), clazz, encodeFunc, decodeFunc, (message, contextSupplier) -> {
            NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                ServerPlayer player = context.getSender();
                handler.accept(player, message);
            });
        });
    }

    public static void initializeClientHandlers() {
        for (ClientboundMessageRegistration<?> message : clientMessageRegistrations) {
            registerClientHandler(message);
        }
    }

    private static <T> void registerClientHandler(ClientboundMessageRegistration<T> messageRegistration) {
        ResourceLocation identifier = messageRegistration.getIdentifier();
        Class<T> clazz = messageRegistration.getClazz();
        BiConsumer<T, FriendlyByteBuf> encodeFunc = messageRegistration.getEncodeFunc();
        Function<FriendlyByteBuf, T> decodeFunc = messageRegistration.getDecodeFunc();
        BiConsumer<Player, T> handler = messageRegistration.getHandler();

        SimpleChannel channel = NetworkChannels.get(identifier.getNamespace());
        channel.registerMessage(discriminator(identifier), clazz, encodeFunc, decodeFunc, (message, contextSupplier) -> {
            NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                handler.accept(BalmClient.getClientPlayer(), message);
            });
        });
    }

    private static int discriminator(ResourceLocation location) {
        return location.getPath().hashCode();
    }
}
