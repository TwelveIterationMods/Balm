package net.blay09.mods.balm.mixin;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.server.ServerReloadedEvent;
import net.minecraft.commands.Commands;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.ServerResources;
import net.minecraft.server.packs.PackResources;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(ServerResources.class)
public class ServerResourcesMixin {
    @Inject(method = "loadResources(Ljava/util/List;Lnet/minecraft/core/RegistryAccess;Lnet/minecraft/commands/Commands$CommandSelection;ILjava/util/concurrent/Executor;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;", at = @At("RETURN"))
    private static void loadResources(List<PackResources> list, RegistryAccess registryAccess, Commands.CommandSelection commandSelection, int i, Executor executor, Executor executor2, CallbackInfoReturnable<CompletableFuture<ServerResources>> cir) {
        cir.getReturnValue().thenApply(resources -> {
            Balm.getEvents().fireEvent(new ServerReloadedEvent(resources));
            return resources;
        });
    }
}
