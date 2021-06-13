package net.blay09.mods.forbic.entity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class ForbicEntity {

    /**
     * Forge provides a patch to cure potion effects specific to a given curative item.
     * Fabric does not provide such patch, so on Fabric this will always clear all effects.
     */
    public static void curePotionEffects(LivingEntity entity, ItemStack curativeItem) {
        entity.removeAllEffects();
    }

}
