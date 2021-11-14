package net.blay09.mods.balm.mixin;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.client.KeyInputEvent;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public class KeyboardHandlerMixin {

    @Final
    @Shadow
    private Minecraft minecraft;

    @Inject(method = "keyPress(JIIII)V", at = @At("TAIL"))
    public void keyPress(long window, int key, int scanCode, int action, int modifiers, CallbackInfo callbackInfo) {
        if (window == this.minecraft.getWindow().getWindow()) {
            Balm.getEvents().fireEvent(new KeyInputEvent(key, scanCode, action, modifiers));
        }
    }

}
