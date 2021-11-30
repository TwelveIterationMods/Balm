package net.blay09.mods.balm.mixin;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.client.FovUpdateEvent;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayer.class)
public class AbstractClientPlayerMixin {

    @Inject(method = "getFieldOfViewModifier()F", at = @At("TAIL"), cancellable = true)
    private void getFieldOfViewModifier(CallbackInfoReturnable<Float> callbackInfo) {
        FovUpdateEvent event = new FovUpdateEvent((LivingEntity) (Object) this);
        Balm.getEvents().fireEvent(event);
        Float override = event.getFov();
        if (override != null) {
            callbackInfo.setReturnValue(override);
        }
    }

}
