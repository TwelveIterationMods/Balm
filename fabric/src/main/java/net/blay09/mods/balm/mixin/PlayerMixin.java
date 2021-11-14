package net.blay09.mods.balm.mixin;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.entity.BalmPlayer;
import net.blay09.mods.balm.api.event.DigSpeedEvent;
import net.blay09.mods.balm.api.event.PlayerAttackEvent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class PlayerMixin implements BalmPlayer {

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

    @Inject(method = "getDestroySpeed(Lnet/minecraft/world/level/block/state/BlockState;)F", at = @At("RETURN"), cancellable = true)
    private void getDestroySpeed(BlockState state, CallbackInfoReturnable<Float> callbackInfo) {
        Player player = (Player) (Object) this;
        float digSpeed = callbackInfo.getReturnValueF();
        DigSpeedEvent event = new DigSpeedEvent(player, state, digSpeed);
        Balm.getEvents().fireEvent(event);
        if (event.isCanceled()) {
            callbackInfo.setReturnValue(-1f);
        } else if (event.getSpeedOverride() != null) {
            callbackInfo.setReturnValue(event.getSpeedOverride());
        }
    }

    @Inject(method = "attack(Lnet/minecraft/world/entity/Entity;)V", at = @At("HEAD"), cancellable = true)
    private void attack(Entity entity, CallbackInfo callbackInfo) {
        Player player = (Player) (Object) this;
        PlayerAttackEvent event = new PlayerAttackEvent(player, entity);
        Balm.getEvents().fireEvent(event);
        if (event.isCanceled()) {
            callbackInfo.cancel();
        }
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
