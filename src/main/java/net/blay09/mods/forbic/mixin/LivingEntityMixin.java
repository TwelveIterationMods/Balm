package net.blay09.mods.forbic.mixin;

import net.blay09.mods.forbic.event.ForbicEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "actuallyHurt(Lnet/minecraft/world/damagesource/DamageSource;F)V", at = @At("HEAD"))
    private void actuallyHurt(DamageSource damageSource, float damageAmount, CallbackInfo callbackInfo) {
        ForbicEvents.LIVING_DAMAGE.invoker().handle((LivingEntity) (Object) this);
    }
}
