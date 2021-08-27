package net.blay09.mods.balm.api.event;

import net.minecraft.world.entity.LivingEntity;

public class LivingFallEvent extends BalmEvent {
    private final LivingEntity entity;
    private float damageMultiplier = 1f;

    public LivingFallEvent(LivingEntity entity) {
        this.entity = entity;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public float getDamageMultiplier() {
        return damageMultiplier;
    }

    public void setDamageMultiplier(float damageMultiplier) {
        this.damageMultiplier = damageMultiplier;
    }
}
