package net.blay09.mods.forbic.client;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

@FunctionalInterface
public interface ForbicScreenFactory<T, S> {
    S create(T menu, Inventory inventory, Component title);
}
