package net.blay09.mods.balm.forge.network;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.menu.BalmMenuProvider;
import net.blay09.mods.balm.api.network.BalmNetworking;
import net.blay09.mods.balm.api.network.ClientboundMessageRegistration;
import net.blay09.mods.balm.api.network.MessageRegistration;
import net.blay09.mods.balm.api.network.ServerboundMessageRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.SimpleChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class ForgeBalmNetworking implements BalmNetworking {

    private static final Logger logger = LoggerFactory.getLogger(ForgeBalmNetworking.class);

    private static final Map<Class<?>, MessageRegistration<?>> messagesByClass = new ConcurrentHashMap<>();
    private static final Map<ResourceLocation, MessageRegistration<?>> messagesByIdentifier = new ConcurrentHashMap<>();
    private static final Map<String, Integer> discriminatorCounter = new ConcurrentHashMap<>();

    private static CustomPayloadEvent.Context replyContext;

    @Override
    public void allowClientOnly(String modId) {
        NetworkChannels.allowClientOnly(modId);
    }

    @Override
    public void allowServerOnly(String modId) {
        NetworkChannels.allowServerOnly(modId);
    }

    @Override
    public void openGui(Player player, MenuProvider menuProvider) {
        if (player instanceof ServerPlayer serverPlayer) {
            if (menuProvider instanceof BalmMenuProvider balmMenuProvider) {
                serverPlayer.openMenu(menuProvider, buf -> balmMenuProvider.writeScreenOpeningData((ServerPlayer) player, buf));
            } else {
                serverPlayer.openMenu(menuProvider);
            }
        }
    }

    @Override
    public <T> void reply(T message) {
        if (replyContext == null) {
            throw new IllegalStateException("No context to reply to");
        }

        MessageRegistration<T> messageRegistration = getMessageRegistrationOrThrow(message);
        ResourceLocation identifier = messageRegistration.getIdentifier();

        SimpleChannel channel = NetworkChannels.get(identifier.getNamespace());
        channel.reply(message, replyContext);
    }

    @Override
    public <T> void sendTo(Player player, T message) {
        MessageRegistration<T> messageRegistration = getMessageRegistrationOrThrow(message);

        ResourceLocation identifier = messageRegistration.getIdentifier();

        SimpleChannel channel = NetworkChannels.get(identifier.getNamespace());
        channel.send(message, PacketDistributor.PLAYER.with((ServerPlayer) player));
    }

    @Override
    public <T> void sendToTracking(ServerLevel world, BlockPos pos, T message) {
        MessageRegistration<T> messageRegistration = getMessageRegistrationOrThrow(message);

        ResourceLocation identifier = messageRegistration.getIdentifier();

        SimpleChannel channel = NetworkChannels.get(identifier.getNamespace());
        channel.send(message, PacketDistributor.TRACKING_CHUNK.with(world.getChunkAt(pos)));
    }

    @Override
    public <T> void sendToTracking(Entity entity, T message) {
        MessageRegistration<T> messageRegistration = getMessageRegistrationOrThrow(message);

        ResourceLocation identifier = messageRegistration.getIdentifier();

        SimpleChannel channel = NetworkChannels.get(identifier.getNamespace());
        channel.send(message, PacketDistributor.TRACKING_ENTITY.with(entity));
    }

    @Override
    public <T> void sendToAll(MinecraftServer server, T message) {
        MessageRegistration<T> messageRegistration = getMessageRegistrationOrThrow(message);

        ResourceLocation identifier = messageRegistration.getIdentifier();

        SimpleChannel channel = NetworkChannels.get(identifier.getNamespace());
        channel.send(message, PacketDistributor.ALL.noArg());
    }

    @Override
    public <T> void sendToServer(T message) {
        if (!Balm.getProxy().isConnectedToServer()) {
            logger.debug("Skipping message {} because we're not connected to a server", message);
            return;
        }

        MessageRegistration<T> messageRegistration = getMessageRegistrationOrThrow(message);

        ResourceLocation identifier = messageRegistration.getIdentifier();

        SimpleChannel channel = NetworkChannels.get(identifier.getNamespace());
        channel.send(message, PacketDistributor.SERVER.noArg());
    }

    @SuppressWarnings("unchecked")
    private <T> MessageRegistration<T> getMessageRegistrationOrThrow(T message) {
        MessageRegistration<T> messageRegistration = (MessageRegistration<T>) messagesByClass.get(message.getClass());
        if (messageRegistration == null) {
            throw new IllegalArgumentException("Cannot send message " + message.getClass() + " as it is not registered");
        }
        return messageRegistration;
    }

    @Override
    public <T> void registerClientboundPacket(ResourceLocation identifier, Class<T> clazz, BiConsumer<T, RegistryFriendlyByteBuf> encodeFunc, Function<RegistryFriendlyByteBuf, T> decodeFunc, BiConsumer<Player, T> handler) {
        ClientboundMessageRegistration<T> messageRegistration = new ClientboundMessageRegistration<>(identifier, clazz, encodeFunc, decodeFunc, handler);

        messagesByClass.put(clazz, messageRegistration);
        messagesByIdentifier.put(identifier, messageRegistration);

        SimpleChannel channel = NetworkChannels.get(identifier.getNamespace());
        channel.messageBuilder(clazz, nextDiscriminator(identifier.getNamespace()), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(decodeFunc)
                .encoder(encodeFunc)
                .consumerMainThread((packet, context) -> handler.accept(Balm.getProxy().getClientPlayer(), packet))
                .add();
    }

    @Override
    public <T> void registerServerboundPacket(ResourceLocation identifier, Class<T> clazz, BiConsumer<T, RegistryFriendlyByteBuf> encodeFunc, Function<RegistryFriendlyByteBuf, T> decodeFunc, BiConsumer<ServerPlayer, T> handler) {
        MessageRegistration<T> messageRegistration = new ServerboundMessageRegistration<>(identifier, clazz, encodeFunc, decodeFunc, handler);

        messagesByClass.put(clazz, messageRegistration);
        messagesByIdentifier.put(identifier, messageRegistration);

        SimpleChannel channel = NetworkChannels.get(identifier.getNamespace());
        channel.messageBuilder(clazz, nextDiscriminator(identifier.getNamespace()), NetworkDirection.PLAY_TO_SERVER)
                .decoder(decodeFunc)
                .encoder(encodeFunc)
                .consumerMainThread((packet, context) -> {
                    replyContext = context;
                    handler.accept(context.getSender(), packet);
                    replyContext = null;
                })
                .add();
    }

    private static int nextDiscriminator(String modId) {
        return discriminatorCounter.compute(modId, (key, prev) -> prev != null ? prev + 1 : 0);
    }
}
