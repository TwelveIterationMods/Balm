package net.blay09.mods.balm.forge.sound;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.sound.BalmSounds;
import net.blay09.mods.balm.forge.DeferredRegisters;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ForgeBalmSounds implements BalmSounds {
    @Override
    public DeferredObject<SoundEvent> register(ResourceLocation identifier) {
        DeferredRegister<SoundEvent> register = DeferredRegisters.get(ForgeRegistries.SOUND_EVENTS, identifier.getNamespace());
        RegistryObject<SoundEvent> registryObject = register.register(identifier.getPath(), () -> SoundEvent.createVariableRangeEvent(identifier));
        return new DeferredObject<>(identifier, registryObject, registryObject::isPresent);
    }
}
