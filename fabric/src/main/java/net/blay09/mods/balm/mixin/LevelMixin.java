package net.blay09.mods.balm.mixin;

import net.blay09.mods.balm.api.block.entity.OnLoadHandler;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Level.class)
public class LevelMixin {
    @Inject(method = "setBlockEntity(Lnet/minecraft/world/level/block/entity/BlockEntity;)V", at = @At("HEAD"))
    private void setBlockEntity(BlockEntity blockEntity, CallbackInfo callbackInfo) {
        if (blockEntity instanceof OnLoadHandler) {
            ((OnLoadHandler) blockEntity).onLoad();
        }
    }
}
