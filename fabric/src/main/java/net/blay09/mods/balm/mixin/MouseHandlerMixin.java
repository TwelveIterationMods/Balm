package net.blay09.mods.balm.mixin;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.client.screen.ScreenMouseEvent;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {

    @Shadow
    private int activeButton;

    @Inject(method = "*", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;mouseDragged(DDIDD)Z"), cancellable = true, require = 1)
    public void onMovePre(Screen screen, double mouseX, double mouseY, double dragX, double dragY, CallbackInfo callbackInfo) {
        ScreenMouseEvent.Drag.Pre event = new ScreenMouseEvent.Drag.Pre(screen, mouseX, mouseY, activeButton, dragX, dragY);
        Balm.getEvents().fireEvent(event);
        if (event.isCanceled()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = "*", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;mouseDragged(DDIDD)Z", shift = At.Shift.AFTER), require = 1)
    public void onMovePost(Screen screen, double mouseX, double mouseY, double dragX, double dragY, CallbackInfo callbackInfo) {
        ScreenMouseEvent.Drag.Post event = new ScreenMouseEvent.Drag.Post(screen, mouseX, mouseY, activeButton, dragX, dragY);
        Balm.getEvents().fireEvent(event);
    }
}
