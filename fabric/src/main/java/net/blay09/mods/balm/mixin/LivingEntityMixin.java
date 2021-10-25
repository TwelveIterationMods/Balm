package net.blay09.mods.balm.mixin;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.LivingDamageEvent;
import net.blay09.mods.balm.api.event.LivingFallEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    private static final ThreadLocal<LivingFallEvent> currentFallEvent = new ThreadLocal<>();

    @Inject(method = "actuallyHurt(Lnet/minecraft/world/damagesource/DamageSource;F)V", at = @At("HEAD"))
    private void actuallyHurt(DamageSource damageSource, float damageAmount, CallbackInfo callbackInfo) {
        Balm.getEvents().fireEvent(new LivingDamageEvent((LivingEntity) (Object) this, damageSource, damageAmount));
    }

    @Inject(method = "causeFallDamage(FFLnet/minecraft/world/damagesource/DamageSource;)Z", at = @At("HEAD"), cancellable = true)
    private void causeFallDamage(float distance, float damageMultiplier, DamageSource damageSource, CallbackInfoReturnable<Boolean> callbackInfo) {
        LivingFallEvent event = new LivingFallEvent((LivingEntity) (Object) this);
        Balm.getEvents().fireEvent(event);
        if (event.isCanceled()) {
            callbackInfo.setReturnValue(false);
        }
        currentFallEvent.set(event);
    }

    @Redirect(method = "causeFallDamage(FFLnet/minecraft/world/damagesource/DamageSource;)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
    private boolean causeFallDamageHurt(LivingEntity entity, DamageSource damageSource, float damage) {
        LivingFallEvent event = currentFallEvent.get();
        float effectiveDamage = damage;
        if (event != null && event.getFallDamageOverride() != null) {
            effectiveDamage = event.getFallDamageOverride();
        }
        return entity.hurt(damageSource, effectiveDamage);
    }
}
