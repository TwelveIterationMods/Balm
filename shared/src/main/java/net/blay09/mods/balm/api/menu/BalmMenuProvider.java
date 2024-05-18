package net.blay09.mods.balm.api.menu;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;

public interface BalmMenuProvider<T> extends MenuProvider {
    T getScreenOpeningData(ServerPlayer player);
}
