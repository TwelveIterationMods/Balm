package net.blay09.mods.balm.api.event;

import net.minecraft.world.entity.LivingEntity;

public class LivingDamageEvent extends BalmEvent {
    private final LivingEntity entity;

    public LivingDamageEvent(LivingEntity entity) {
        this.entity = entity;
    }

    public LivingEntity getEntity() {
        return entity;
    }
}
