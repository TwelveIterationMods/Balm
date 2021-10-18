package net.blay09.mods.balm.api;

import net.blay09.mods.balm.api.block.BalmBlockEntities;
import net.blay09.mods.balm.api.block.BalmBlocks;
import net.blay09.mods.balm.api.command.BalmCommands;
import net.blay09.mods.balm.api.config.BalmConfig;
import net.blay09.mods.balm.api.entity.BalmEntities;
import net.blay09.mods.balm.api.event.BalmEvents;
import net.blay09.mods.balm.api.event.EventPriority;
import net.blay09.mods.balm.api.item.BalmItems;
import net.blay09.mods.balm.api.menu.BalmMenus;
import net.blay09.mods.balm.api.network.BalmNetworking;
import net.blay09.mods.balm.api.provider.BalmProviders;
import net.blay09.mods.balm.api.sound.BalmSounds;
import net.blay09.mods.balm.api.world.BalmWorldGen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ServerResources;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;

import java.util.function.Consumer;
import java.util.function.Function;

public interface BalmRuntime {
    BalmConfig getConfig();

    BalmEvents getEvents();

    BalmWorldGen getWorldGen();

    BalmBlocks getBlocks();

    BalmBlockEntities getBlockEntities();

    BalmItems getItems();

    BalmMenus getMenus();

    BalmNetworking getNetworking();

    BalmHooks getHooks();

    BalmRegistries getRegistries();

    BalmSounds getSounds();

    BalmEntities getEntities();

    BalmProviders getProviders();

    BalmCommands getCommands();

    boolean isModLoaded(String modId);

    void initialize(String modId, Runnable initializer);

    void initializeIfLoaded(String modId, String className);

    void addServerReloadListener(ResourceLocation identifier, PreparableReloadListener reloadListener);
    void addServerReloadListener(ResourceLocation identifier, Consumer<ResourceManager> reloadListener);
}
