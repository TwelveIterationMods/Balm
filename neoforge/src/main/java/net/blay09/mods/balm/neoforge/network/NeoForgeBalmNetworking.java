package net.blay09.mods.balm.neoforge.network;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.menu.BalmMenuProvider;
import net.blay09.mods.balm.api.network.BalmNetworking;
import net.blay09.mods.balm.api.network.ClientboundMessageRegistration;
import net.blay09.mods.balm.api.network.MessageRegistration;
import net.blay09.mods.balm.api.network.ServerboundMessageRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.handling.IReplyHandler;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class NeoForgeBalmNetworking implements BalmNetworking {

    private static final Logger logger = LoggerFactory.getLogger(NeoForgeBalmNetworking.class);

    /**
     * TODO remove in 1.20.5 and make all packets implement CustomPacketPayload
     *
     * @deprecated workaround to account for this mid-version breaking change without having to update 152518 mods
     */
    @Deprecated
    private record WrappedPacket(ResourceLocation id, Object message, BiConsumer<Object, FriendlyByteBuf> encoder) implements CustomPacketPayload {
        @Override
        public void write(FriendlyByteBuf buf) {
            encoder.accept(message, buf);
        }

        @Override
        public ResourceLocation id() {
            return id;
        }
    }

    private static class Registrations {
        private final String modId;
        private final Map<ResourceLocation, MessageRegistration<?>> messagesByIdentifier = new ConcurrentHashMap<>();
        private boolean optional;

        private Registrations(String modId) {
            this.modId = modId;
        }

        @SubscribeEvent
        @SuppressWarnings("unchecked")
        public void registerPayloadHandlers(final RegisterPayloadHandlerEvent event) {
            var registrar = event.registrar(modId);
            if (optional) {
                registrar = registrar.optional();
            }
            for (final var entry : messagesByIdentifier.entrySet()) {
                final var messageId = entry.getKey();
                final var messageRegistration = entry.getValue();
                registrar = registrar.play(messageId, (FriendlyByteBuf.Reader<CustomPacketPayload>) buf -> {
                    final var message = messageRegistration.getDecodeFunc().apply(buf);
                    return new WrappedPacket(messageId, message, (BiConsumer<Object, FriendlyByteBuf>) messageRegistration.getEncodeFunc());
                }, it -> {
                    if (messageRegistration instanceof ServerboundMessageRegistration<?> serverboundMessageRegistration) {
                        it.server((payload, context) -> context.workHandler().execute(() -> {
                            replyHandler = context.replyHandler();
                            handleServerboundPacket(serverboundMessageRegistration, payload, context);
                            replyHandler = null;
                        }));
                    } else if (messageRegistration instanceof ClientboundMessageRegistration<?> clientboundMessageRegistration) {
                        it.client(((payload, context) -> context.workHandler().execute(() -> {
                            replyHandler = context.replyHandler();
                            handleClientboundPacket(clientboundMessageRegistration, payload, context);
                            replyHandler = null;
                        })));
                    }
                });
            }
        }

        public void allowClientOnly() {
            optional = true;
        }

        public void allowServerOnly() {
            optional = true;
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> void handleServerboundPacket(ServerboundMessageRegistration<T> registration, CustomPacketPayload payload, PlayPayloadContext context) {
        final var player = context.player().orElse(null);
        if (payload instanceof WrappedPacket wrappedPacket && player instanceof ServerPlayer serverPlayer) {
            registration.getHandler().accept(serverPlayer, (T) wrappedPacket.message());
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> void handleClientboundPacket(ClientboundMessageRegistration<T> registration, CustomPacketPayload payload, PlayPayloadContext context) {
        if (payload instanceof WrappedPacket wrappedPacket) {
            registration.getHandler().accept(Balm.getProxy().getClientPlayer(), (T) wrappedPacket.message());
        }
    }

    private final Map<String, Registrations> registrations = new ConcurrentHashMap<>();
    private final Map<Class<?>, MessageRegistration<?>> messagesByClass = new ConcurrentHashMap<>();

    private static IReplyHandler replyHandler;

    @Override
    public void allowClientOnly(String modId) {
        getRegistrations(modId).allowClientOnly();
    }

    @Override
    public void allowServerOnly(String modId) {
        getRegistrations(modId).allowServerOnly();
    }

    @Override
    public void openGui(Player player, MenuProvider menuProvider) {
        if (player instanceof ServerPlayer serverPlayer) {
            if (menuProvider instanceof BalmMenuProvider balmMenuProvider) {
                serverPlayer.openMenu(menuProvider, buf -> balmMenuProvider.writeScreenOpeningData(serverPlayer, buf));
            } else {
                serverPlayer.openMenu(menuProvider);
            }
        }
    }

    @Override
    public <T> void reply(T message) {
        if (replyHandler == null) {
            throw new IllegalStateException("No context to reply to");
        }

        MessageRegistration<T> messageRegistration = getMessageRegistrationOrThrow(message);
        ResourceLocation identifier = messageRegistration.getIdentifier();

        replyHandler.send(new WrappedPacket(identifier, message, (BiConsumer<Object, FriendlyByteBuf>) messageRegistration.getEncodeFunc()));
    }

    @Override
    public <T> void sendTo(Player player, T message) {
        if (player instanceof ServerPlayer serverPlayer) {
            sendPacket(PacketDistributor.PLAYER.with(serverPlayer), message);
        }
    }

    @Override
    public <T> void sendToTracking(ServerLevel level, BlockPos pos, T message) {
        sendPacket(PacketDistributor.TRACKING_CHUNK.with(level.getChunkAt(pos)), message);
    }

    @Override
    public <T> void sendToTracking(Entity entity, T message) {
        sendPacket(PacketDistributor.TRACKING_ENTITY.with(entity), message);
    }

    @Override
    public <T> void sendToAll(MinecraftServer server, T message) {
        sendPacket(PacketDistributor.ALL.noArg(), message);
    }

    @Override
    public <T> void sendToServer(T message) {
        if (!Balm.getProxy().isConnectedToServer()) {
            logger.debug("Skipping message {} because we're not connected to a server", message);
            return;
        }

        sendPacket(PacketDistributor.SERVER.noArg(), message);
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
    public <T> void registerClientboundPacket(ResourceLocation identifier, Class<T> clazz, BiConsumer<T, FriendlyByteBuf> encodeFunc, Function<FriendlyByteBuf, T> decodeFunc, BiConsumer<Player, T> handler) {
        ClientboundMessageRegistration<T> messageRegistration = new ClientboundMessageRegistration<>(identifier, clazz, encodeFunc, decodeFunc, handler);
        final var registrations = getActiveRegistrations();
        messagesByClass.put(clazz, messageRegistration);
        registrations.messagesByIdentifier.put(identifier, messageRegistration);
    }

    @Override
    public <T> void registerServerboundPacket(ResourceLocation identifier, Class<T> clazz, BiConsumer<T, FriendlyByteBuf> encodeFunc, Function<FriendlyByteBuf, T> decodeFunc, BiConsumer<ServerPlayer, T> handler) {
        MessageRegistration<T> messageRegistration = new ServerboundMessageRegistration<>(identifier, clazz, encodeFunc, decodeFunc, handler);
        final var registrations = getActiveRegistrations();
        messagesByClass.put(clazz, messageRegistration);
        registrations.messagesByIdentifier.put(identifier, messageRegistration);
    }

    private <T> void sendPacket(PacketDistributor.PacketTarget target, T message) {
        MessageRegistration<T> messageRegistration = getMessageRegistrationOrThrow(message);
        target.send(new CustomPacketPayload() {
            @Override
            public void write(FriendlyByteBuf buf) {
                messageRegistration.getEncodeFunc().accept(message, buf);
            }

            @Override
            public ResourceLocation id() {
                return messageRegistration.getIdentifier();
            }
        });
    }

    public void register(String modId, IEventBus eventBus) {
        eventBus.register(getRegistrations(modId));
    }

    private Registrations getActiveRegistrations() {
        return getRegistrations(ModLoadingContext.get().getActiveNamespace());
    }

    private Registrations getRegistrations(String modId) {
        return registrations.computeIfAbsent(modId, it -> new Registrations(modId));
    }
}