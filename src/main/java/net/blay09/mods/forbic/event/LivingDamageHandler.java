package net.blay09.mods.forbic.event;

import net.minecraft.world.entity.LivingEntity;

@FunctionalInterface
public interface LivingDamageHandler {
    void handle(LivingEntity entity);
}
