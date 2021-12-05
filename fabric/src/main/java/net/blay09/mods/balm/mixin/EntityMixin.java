package net.blay09.mods.balm.mixin;

import net.blay09.mods.balm.api.entity.BalmEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin implements BalmEntity {

    private CompoundTag balmData = new CompoundTag();

    @Inject(method = "load(Lnet/minecraft/nbt/CompoundTag;)V", at = @At("HEAD"))
    private void load(CompoundTag compound, CallbackInfo callbackInfo) {
        if (compound.contains("BalmData")) {
            balmData = compound.getCompound("BalmData");
        }
    }

    @Inject(method = "saveWithoutId(Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/nbt/CompoundTag;", at = @At("HEAD"))
    private void saveWithoutId(CompoundTag compound, CallbackInfoReturnable<CompoundTag> callbackInfo) {
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
