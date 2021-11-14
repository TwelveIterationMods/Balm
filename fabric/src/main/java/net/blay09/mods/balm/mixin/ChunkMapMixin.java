package net.blay09.mods.balm.mixin;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.ChunkTrackingEvent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkMap.class)
public class ChunkMapMixin {

    @Final
    @Shadow
    ServerLevel level;

    @Inject(method = "updateChunkTracking(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/world/level/ChunkPos;[Lnet/minecraft/network/protocol/Packet;ZZ)V", at = @At("HEAD"))
    void updateChunkTracking(ServerPlayer player, ChunkPos chunkPos, Packet<?>[] packets, boolean wasLoaded, boolean shouldLoad, CallbackInfo callbackInfo) {
        if (wasLoaded != shouldLoad && player.level == level) {
            if (shouldLoad) {
                Balm.getEvents().fireEvent(new ChunkTrackingEvent.Start(level, player, chunkPos));
            } else {
                Balm.getEvents().fireEvent(new ChunkTrackingEvent.Stop(level, player, chunkPos));
            }
        }
    }

}
