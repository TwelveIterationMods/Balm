package net.blay09.mods.forbic.menu;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public interface ForbicMenuProvider extends ExtendedScreenHandlerFactory {
    @Override
    default void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {}
}
