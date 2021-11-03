package net.blay09.mods.balm.mixin;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.server.ServerReloadedEvent;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Inject(method = "reloadResources(Ljava/util/Collection;)Ljava/util/concurrent/CompletableFuture;", at = @At("RETURN"), cancellable = true)
    public void reloadResources(Collection<String> selectedPacks, CallbackInfoReturnable<CompletableFuture<Void>> callbackInfo) {
        MinecraftServer server = (MinecraftServer) (Object) this;
        callbackInfo.getReturnValue().thenAcceptAsync(it -> Balm.getEvents().fireEvent(new ServerReloadedEvent(server)));
    }
}
