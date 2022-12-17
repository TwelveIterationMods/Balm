package net.blay09.mods.balm.mixin;

import net.minecraft.world.damagesource.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(DamageSource.class)
public interface DamageSourceAccessor {
    @Invoker("<init>")
    static DamageSource create(String message) {
        throw new AssertionError();
    }

    @Invoker
    DamageSource callBypassMagic();

    @Invoker
    DamageSource callBypassArmor();

    @Invoker
    DamageSource callBypassInvul();
}
