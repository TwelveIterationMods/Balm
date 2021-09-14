package net.blay09.mods.balm.forge.client.rendering;

import net.blay09.mods.balm.api.client.rendering.BalmTextures;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class ForgeBalmTextures implements BalmTextures {

    private static class TextureRegistration {
        private final ResourceLocation atlas;
        private final ResourceLocation location;

        public TextureRegistration(ResourceLocation atlas, ResourceLocation location) {
            this.atlas = atlas;
            this.location = location;
        }

        public ResourceLocation getAtlas() {
            return atlas;
        }

        public ResourceLocation getLocation() {
            return location;
        }
    }

    private final List<TextureRegistration> textures = new ArrayList<>();

    @Override
    public void addSprite(ResourceLocation atlas, ResourceLocation location) {
        textures.add(new TextureRegistration(atlas, location));
    }

    @SubscribeEvent
    public void registerIconsPre(TextureStitchEvent.Pre event) {
        for (TextureRegistration texture : textures) {
            if (event.getMap().location().equals(texture.getAtlas())) {
                event.addSprite(texture.getLocation());
            }
        }
    }
}
