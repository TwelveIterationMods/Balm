package net.blay09.mods.balm.forge.client.rendering;

import net.blay09.mods.balm.api.client.rendering.BalmTextures;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

    private static class Registrations {
        private final List<TextureRegistration> textures = new ArrayList<>();

        @SubscribeEvent
        public void registerIconsPre(TextureStitchEvent.Pre event) {
            for (TextureRegistration texture : textures) {
                if (event.getMap().location().equals(texture.getAtlas())) {
                    event.addSprite(texture.getLocation());
                }
            }
        }
    }

    private final Map<String, Registrations> registrations = new ConcurrentHashMap<>();

    @Override
    public void addSprite(ResourceLocation atlas, ResourceLocation location) {
        getActiveRegistrations().textures.add(new TextureRegistration(atlas, location));
    }

    public void register() {
        FMLJavaModLoadingContext.get().getModEventBus().register(getActiveRegistrations());
    }

    private Registrations getActiveRegistrations() {
        return registrations.computeIfAbsent(ModLoadingContext.get().getActiveNamespace(), it -> new Registrations());
    }
}
