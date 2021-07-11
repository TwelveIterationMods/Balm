package net.blay09.mods.balm.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class BalmItem extends Item {
    public BalmItem(Properties properties) {
        super(properties);
    }

    public boolean balmShowDurabilityBar(ItemStack stack) {
        return stack.isDamaged();
    }

    public double balmGetDurabilityForDisplay(ItemStack stack) {
        return (double) stack.getDamageValue() / (double) stack.getMaxDamage();
    }
}
