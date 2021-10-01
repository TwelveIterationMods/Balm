package net.blay09.mods.balm.fabric.sound;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.sound.BalmSounds;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class FabricBalmSounds implements BalmSounds {
    @Override
    public DeferredObject<SoundEvent> register(ResourceLocation identifier) {
        return new DeferredObject<>(identifier, () -> {
            SoundEvent soundEvent = new SoundEvent(identifier);
            return Registry.register(Registry.SOUND_EVENT, identifier, soundEvent);
        }).resolveImmediately();
    }
}
