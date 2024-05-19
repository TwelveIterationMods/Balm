package net.blay09.mods.balm.api.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;

public interface BalmMenuProvider<TPayload> extends MenuProvider {
    TPayload getScreenOpeningData(ServerPlayer player);
    StreamCodec<FriendlyByteBuf, TPayload> getCodec();
}
