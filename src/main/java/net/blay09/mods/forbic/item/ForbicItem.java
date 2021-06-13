package net.blay09.mods.forbic.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ForbicItem extends Item {
    public ForbicItem(Properties properties) {
        super(properties);
    }

    public boolean forbicShowDurabilityBar(ItemStack stack) {
        return stack.isDamaged();
    }

    public double forbicGetDurabilityForDisplay(ItemStack stack) {
        return (double) stack.getDamageValue() / (double) stack.getMaxDamage();
    }
}
