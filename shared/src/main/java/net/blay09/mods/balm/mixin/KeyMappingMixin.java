package net.blay09.mods.balm.mixin;

import net.blay09.mods.balm.api.client.BalmClient;
import net.minecraft.client.KeyMapping;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(KeyMapping.class)
public class KeyMappingMixin {
    @Inject(method = "same(Lnet/minecraft/client/KeyMapping;)Z", cancellable = true, at = @At("HEAD"))
    public void same(KeyMapping keyMapping, CallbackInfoReturnable<Boolean> callbackInfo) {
        KeyMapping thisKeyMapping = (KeyMapping) (Object) this;
        BalmClient.getKeyMappings().conflictsWith(thisKeyMapping, keyMapping).ifPresent(callbackInfo::setReturnValue);
    }
}
