package net.blay09.mods.balm.mixin;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.LivingDamageEvent;
import net.blay09.mods.balm.api.event.LivingFallEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
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
    }
}
