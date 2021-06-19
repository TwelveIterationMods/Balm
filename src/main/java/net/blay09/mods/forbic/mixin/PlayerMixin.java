package net.blay09.mods.forbic.mixin;

import net.blay09.mods.forbic.api.IForbicPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerMixin implements IForbicPlayer {

    private CompoundTag forbicData = new CompoundTag();

    @Inject(method = "readAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;F)V", at = @At("HEAD"))
    private void readAdditionalSaveData(CompoundTag compound, CallbackInfo callbackInfo) {
        forbicData = compound.getCompound("ForbicData");
    }

    @Inject(method = "addAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;F)V", at = @At("HEAD"))
    private void addAdditionalSaveData(CompoundTag compound, CallbackInfo callbackInfo) {
        compound.put("ForbicData", forbicData);
    }

    @Override
    public CompoundTag getForbicData() {
        return forbicData;
    }

    @Override
    public void setForbicData(CompoundTag forbicData) {
        this.forbicData = forbicData;
    }
}
