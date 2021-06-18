package net.blay09.mods.forbic.client;

import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.resources.ResourceLocation;

public class ForbicTextures {
    protected static void addSprite(ResourceLocation atlas, ResourceLocation location) {
        ClientSpriteRegistryCallback.event(atlas).register((atlasTexture, registry) -> registry.register(location));
    }
}
