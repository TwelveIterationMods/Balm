package net.blay09.mods.balm.mixin;

import net.blay09.mods.balm.api.client.BalmClient;
import net.blay09.mods.balm.fabric.client.rendering.FabricBalmModels;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModelManager.class)
public class ModelManagerMixin {
    @Inject(method = "apply(Lnet/minecraft/client/resources/model/ModelBakery;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/block/BlockModelShaper;rebuildCache()V"))
    private void setBlockEntity(ModelBakery modelBakery, ResourceManager resourceManager, ProfilerFiller profilerFiller, CallbackInfo callbackInfo) {
        ((FabricBalmModels) BalmClient.getModels()).onModelBake(modelBakery);
    }
}
