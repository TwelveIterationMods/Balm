package net.blay09.mods.balm.event;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

@FunctionalInterface
public interface ItemCraftedHandler {
    void handle(Player player, ItemStack itemStack, Container craftMatrix);
}
