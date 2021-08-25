package net.blay09.mods.balm.api.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public interface BalmMenuProviderContract {
    void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf);
}
