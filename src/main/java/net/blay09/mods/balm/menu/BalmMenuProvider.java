package net.blay09.mods.balm.menu;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public interface BalmMenuProvider extends ExtendedScreenHandlerFactory {
    @Override
    default void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {}
}
