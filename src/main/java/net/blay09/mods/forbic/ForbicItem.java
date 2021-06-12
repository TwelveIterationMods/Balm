package net.blay09.mods.forbic;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ForbicItem extends Item {
    public ForbicItem(Properties properties) {
        super(properties);
    }

    public boolean showDurabilityBar(ItemStack stack) {
        return stack.isDamaged();
    }

    public double getDurabilityForDisplay(ItemStack stack) {
        return (double) stack.getDamageValue() / (double) stack.getMaxDamage();
    }
}
