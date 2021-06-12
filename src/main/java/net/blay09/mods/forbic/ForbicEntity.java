package net.blay09.mods.forbic;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class ForbicEntity {

    public static void curePotionEffects(LivingEntity entity, ItemStack curativeItem) {
        entity.removeAllEffects();
    }
}
