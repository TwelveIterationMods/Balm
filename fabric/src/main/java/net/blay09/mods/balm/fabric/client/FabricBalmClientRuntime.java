package net.blay09.mods.balm.fabric.client;

import net.blay09.mods.balm.api.client.BalmClientRuntime;
import net.blay09.mods.balm.api.client.keymappings.BalmKeyMappings;
import net.blay09.mods.balm.api.client.rendering.BalmRenderers;
import net.blay09.mods.balm.api.client.rendering.BalmTextures;
import net.blay09.mods.balm.api.client.screen.BalmScreens;
import net.blay09.mods.balm.fabric.client.keymappings.FabricBalmKeyMappings;
import net.blay09.mods.balm.fabric.client.rendering.FabricBalmRenderers;
import net.blay09.mods.balm.fabric.client.rendering.FabricBalmTextures;
import net.blay09.mods.balm.fabric.client.screen.FabricBalmScreens;

public class FabricBalmClientRuntime implements BalmClientRuntime {

    private final BalmRenderers renderers = new FabricBalmRenderers();
    private final BalmTextures textures = new FabricBalmTextures();
    private final BalmScreens screens = new FabricBalmScreens();
    private final BalmKeyMappings keyMappings = new FabricBalmKeyMappings();

    @Override
    public BalmRenderers getRenderers() {
        return renderers;
    }

    @Override
    public BalmTextures getTextures() {
        return textures;
    }

    @Override
    public BalmScreens getScreens() {
        return screens;
    }

    @Override
    public BalmKeyMappings getKeyMappings() {
        return keyMappings;
    }
}
