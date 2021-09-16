package net.blay09.mods.balm.api.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;

public interface BalmMenuProvider extends MenuProvider, BalmMenuProviderContract {
    @Override
    default void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {}
}
