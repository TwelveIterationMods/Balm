package net.blay09.mods.balm.mixin;

import net.blay09.mods.balm.api.IBalmPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerMixin implements IBalmPlayer {

    private CompoundTag balmData = new CompoundTag();

    @Inject(method = "readAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V", at = @At("HEAD"))
    private void readAdditionalSaveData(CompoundTag compound, CallbackInfo callbackInfo) {
        if (compound.contains("BalmData")) {
            balmData = compound.getCompound("BalmData");
        } else {
            balmData = compound.getCompound("ForbicData"); // backwards compat for player data pre-rename
        }
    }

    @Inject(method = "addAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V", at = @At("HEAD"))
    private void addAdditionalSaveData(CompoundTag compound, CallbackInfo callbackInfo) {
        compound.put("BalmData", balmData);
    }

    @Override
    public CompoundTag getBalmData() {
        return balmData;
    }

    @Override
    public void setBalmData(CompoundTag tag) {
        this.balmData = tag;
    }
}
