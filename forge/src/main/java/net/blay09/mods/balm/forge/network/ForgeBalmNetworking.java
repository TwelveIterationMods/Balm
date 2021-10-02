package net.blay09.mods.balm.forge.network;

import net.blay09.mods.balm.api.client.BalmClient;
import net.blay09.mods.balm.api.menu.BalmMenuProvider;
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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fmllegacy.network.NetworkDirection;
import net.minecraftforge.fmllegacy.network.NetworkEvent;
import net.minecraftforge.fmllegacy.network.NetworkHooks;
import net.minecraftforge.fmllegacy.network.PacketDistributor;
import net.minecraftforge.fmllegacy.network.simple.SimpleChannel;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class ForgeBalmNetworking implements BalmNetworking {

    private static final Map<Class<?>, MessageRegistration<?>> messagesByClass = new HashMap<>();
    private static final Map<ResourceLocation, MessageRegistration<?>> messagesByIdentifier = new HashMap<>();

    @Override
    public void openGui(Player player, MenuProvider menuProvider) {
        if (player instanceof ServerPlayer) {
            if (menuProvider instanceof BalmMenuProvider) {
                NetworkHooks.openGui((ServerPlayer) player, menuProvider, buf -> ((BalmMenuProvider) menuProvider).writeScreenOpeningData((ServerPlayer) player, buf));
            } else {
                NetworkHooks.openGui((ServerPlayer) player, menuProvider);
            }
        }
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
    public <T> void sendToTracking(Entity entity, T message) {
        MessageRegistration<T> messageRegistration = (MessageRegistration<T>) messagesByClass.get(message.getClass());
        ResourceLocation identifier = messageRegistration.getIdentifier();

        SimpleChannel channel = NetworkChannels.get(identifier.getNamespace());
        channel.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), message);
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

        SimpleChannel channel = NetworkChannels.get(identifier.getNamespace());
        channel.registerMessage(discriminator(identifier), clazz, encodeFunc, decodeFunc, (message, contextSupplier) -> {
            NetworkEvent.Context context = contextSupplier.get();
            if (context.getDirection() != NetworkDirection.PLAY_TO_CLIENT) {
                return;
            }

            context.enqueueWork(() -> {
                handler.accept(BalmClient.getClientPlayer(), message);
            });
            context.setPacketHandled(true);
        });
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
            context.setPacketHandled(true);
        });
    }

    private static int discriminator(ResourceLocation location) {
        return location.getPath().hashCode();
    }
}
