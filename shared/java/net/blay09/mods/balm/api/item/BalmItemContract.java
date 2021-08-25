package net.blay09.mods.balm.api.item;

import net.minecraft.world.item.ItemStack;

public interface BalmItemContract {
    boolean balmShowDurabilityBar(ItemStack stack);

    double balmGetDurabilityForDisplay(ItemStack stack);
}
