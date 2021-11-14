package net.blay09.mods.balm.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.client.GuiDrawEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ChatComponent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatComponent.class)
public class ChatComponentMixin {

    @Final
    @Shadow
    private Minecraft minecraft;

    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;I)V", at = @At("HEAD"), cancellable = true)
    public void renderPre(PoseStack poseStack, int tickCount, CallbackInfo callbackInfo) {
        GuiDrawEvent.Pre event = new GuiDrawEvent.Pre(minecraft.getWindow(), poseStack, GuiDrawEvent.Element.CHAT);
        Balm.getEvents().fireEvent(event);
        if (event.isCanceled()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;I)V", at = @At("TAIL"))
    public void renderPost(PoseStack poseStack, int tickCount, CallbackInfo callbackInfo) {
        Balm.getEvents().fireEvent(new GuiDrawEvent.Post(minecraft.getWindow(), poseStack, GuiDrawEvent.Element.CHAT));
    }
}
