package net.blay09.mods.balm.fabric.client.rendering;

import net.blay09.mods.balm.api.client.rendering.BalmTextures;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.resources.ResourceLocation;

public class FabricBalmTextures implements BalmTextures {
    @Override
    public void addSprite(ResourceLocation atlas, ResourceLocation location) {
        ClientSpriteRegistryCallback.event(atlas).register((atlasTexture, registry) -> registry.register(location));
    }
}
