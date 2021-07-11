package net.blay09.mods.balm.event.client;

import net.minecraft.world.entity.LivingEntity;

@FunctionalInterface
public interface FovUpdateHandler {
    Float handle(LivingEntity entity);
}
