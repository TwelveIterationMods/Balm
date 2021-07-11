package net.blay09.mods.balm.client.rendering;

import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.resources.ResourceLocation;

public class BalmTextures {
    public static void addSprite(ResourceLocation atlas, ResourceLocation location) {
        ClientSpriteRegistryCallback.event(atlas).register((atlasTexture, registry) -> registry.register(location));
    }
}
