package net.blay09.mods.balm.fabric;

import net.blay09.mods.balm.api.BalmHooks;
import net.blay09.mods.balm.api.BalmRegistries;
import net.blay09.mods.balm.api.BalmRuntime;
import net.blay09.mods.balm.api.block.BalmBlockEntities;
import net.blay09.mods.balm.api.block.BalmBlocks;
import net.blay09.mods.balm.api.config.BalmConfig;
import net.blay09.mods.balm.api.entity.BalmEntities;
import net.blay09.mods.balm.api.event.BalmEvents;
import net.blay09.mods.balm.api.event.FabricBalmCommonEvents;
import net.blay09.mods.balm.api.event.FabricBalmEvents;
import net.blay09.mods.balm.api.item.BalmItems;
import net.blay09.mods.balm.api.menu.BalmMenus;
import net.blay09.mods.balm.api.network.BalmNetworking;
import net.blay09.mods.balm.api.provider.BalmProviders;
import net.blay09.mods.balm.api.sound.BalmSounds;
import net.blay09.mods.balm.fabric.block.FabricBalmBlocks;
import net.blay09.mods.balm.api.world.BalmWorldGen;
import net.blay09.mods.balm.fabric.block.entity.FabricBalmBlockEntities;
import net.blay09.mods.balm.fabric.config.FabricBalmConfig;
import net.blay09.mods.balm.fabric.entity.FabricBalmEntities;
import net.blay09.mods.balm.fabric.item.FabricBalmItems;
import net.blay09.mods.balm.fabric.menu.FabricBalmMenus;
import net.blay09.mods.balm.fabric.network.FabricBalmNetworking;
import net.blay09.mods.balm.fabric.provider.FabricBalmProviders;
import net.blay09.mods.balm.fabric.sound.FabricBalmSounds;
import net.blay09.mods.balm.fabric.world.FabricBalmWorldGen;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Consumer;

public class FabricBalmRuntime implements BalmRuntime {
    private final BalmWorldGen worldGen = new FabricBalmWorldGen();
    private final BalmBlocks blocks = new FabricBalmBlocks();
    private final BalmBlockEntities blockEntities = new FabricBalmBlockEntities();
    private final FabricBalmEvents events = new FabricBalmEvents();
    private final BalmItems items = new FabricBalmItems();
    private final BalmMenus menus = new FabricBalmMenus();
    private final BalmNetworking networking = new FabricBalmNetworking();
    private final BalmConfig config = new FabricBalmConfig();
    private final BalmHooks hooks = new FabricBalmHooks();
    private final BalmRegistries registries = new FabricBalmRegistries();
    private final BalmSounds sounds = new FabricBalmSounds();
    private final BalmEntities entities = new FabricBalmEntities();
    private final BalmProviders providers = new FabricBalmProviders();

    public FabricBalmRuntime() {
        FabricBalmCommonEvents.registerEvents(events);
    }

    @Override
    public BalmConfig getConfig() {
        return config;
    }

    @Override
    public BalmEvents getEvents() {
        return events;
    }

    @Override
    public BalmWorldGen getWorldGen() {
        return worldGen;
    }

    @Override
    public BalmBlocks getBlocks() {
        return blocks;
    }

    @Override
    public BalmBlockEntities getBlockEntities() {
        return blockEntities;
    }

    @Override
    public BalmItems getItems() {
        return items;
    }

    @Override
    public BalmMenus getMenus() {
        return menus;
    }

    @Override
    public BalmNetworking getNetworking() {
        return networking;
    }

    @Override
    public BalmHooks getHooks() {
        return hooks;
    }

    @Override
    public BalmRegistries getRegistries() {
        return registries;
    }

    @Override
    public BalmSounds getSounds() {
        return sounds;
    }

    @Override
    public BalmEntities getEntities() {
        return entities;
    }

    @Override
    public BalmProviders getProviders() {
        return providers;
    }

    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public void initialize(String modId) {
    }

    @Override
    public void initializeIfLoaded(String modId, String className) {
        if (isModLoaded(modId)) {
            try {
                Class.forName(className).getConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void addServerReloadListener(ResourceLocation identifier, Consumer<ResourceManager> reloadListener) {
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override
            public void onResourceManagerReload(ResourceManager resourceManager) {
                reloadListener.accept(resourceManager);
            }

            @Override
            public ResourceLocation getFabricId() {
                return identifier;
            }
        });
    }
}
