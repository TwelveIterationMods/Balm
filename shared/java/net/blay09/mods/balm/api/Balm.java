package net.blay09.mods.balm.api;

import net.blay09.mods.balm.api.block.BalmBlockEntities;
import net.blay09.mods.balm.api.block.BalmBlocks;
import net.blay09.mods.balm.api.command.BalmCommands;
import net.blay09.mods.balm.api.config.BalmConfig;
import net.blay09.mods.balm.api.entity.BalmEntities;
import net.blay09.mods.balm.api.event.BalmEvents;
import net.blay09.mods.balm.api.item.BalmItems;
import net.blay09.mods.balm.api.loot.BalmLootTables;
import net.blay09.mods.balm.api.menu.BalmMenus;
import net.blay09.mods.balm.api.network.BalmNetworking;
import net.blay09.mods.balm.api.provider.BalmProviders;
import net.blay09.mods.balm.api.proxy.SidedProxy;
import net.blay09.mods.balm.api.sound.BalmSounds;
import net.blay09.mods.balm.api.stats.BalmStats;
import net.blay09.mods.balm.api.world.BalmWorldGen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;

import java.util.function.Consumer;

public class Balm {
    private static BalmRuntime runtime;
    private static SidedProxy<BalmProxy> proxy;

    private static BalmRuntime getRuntime() {
        if (runtime == null) {
            runtime = BalmRuntimeSpi.create();
        }
        return runtime;
    }

    @Deprecated
    public static void initialize(String modId) {
        getRuntime().initialize(modId, () -> {
        });
    }

    public static void initialize(String modId, Runnable initializer) {
        getRuntime().initialize(modId, initializer);
    }

    public static boolean isModLoaded(String modId) {
        return getRuntime().isModLoaded(modId);
    }

    public static String getModName(String modId) {
        return getRuntime().getModName(modId);
    }

    public static <T> SidedProxy<T> sidedProxy(String commonName, String clientName) {
        return getRuntime().sidedProxy(commonName, clientName);
    }

    public static void initializeIfLoaded(String modId, String className) {
        getRuntime().initializeIfLoaded(modId, className);
    }

    public static void addServerReloadListener(ResourceLocation identifier, PreparableReloadListener reloadListener) {
        getRuntime().addServerReloadListener(identifier, reloadListener);
    }

    public static void addServerReloadListener(ResourceLocation identifier, Consumer<ResourceManager> reloadListener) {
        getRuntime().addServerReloadListener(identifier, reloadListener);
    }

    public static BalmProxy getProxy() {
        if (proxy == null) {
            proxy = sidedProxy("net.blay09.mods.balm.api.BalmProxy", "net.blay09.mods.balm.api.client.BalmClientProxy");
        }
        return proxy.get();
    }

    public static BalmEvents getEvents() {
        return getRuntime().getEvents();
    }

    public static BalmConfig getConfig() {
        return getRuntime().getConfig();
    }

    public static BalmNetworking getNetworking() {
        return getRuntime().getNetworking();
    }

    public static BalmWorldGen getWorldGen() {
        return getRuntime().getWorldGen();
    }

    public static BalmBlocks getBlocks() {
        return getRuntime().getBlocks();
    }

    public static BalmBlockEntities getBlockEntities() {
        return getRuntime().getBlockEntities();
    }

    public static BalmItems getItems() {
        return getRuntime().getItems();
    }

    public static BalmMenus getMenus() {
        return getRuntime().getMenus();
    }

    public static BalmHooks getHooks() {
        return getRuntime().getHooks();
    }

    public static BalmRegistries getRegistries() {
        return getRuntime().getRegistries();
    }

    public static BalmSounds getSounds() {
        return getRuntime().getSounds();
    }

    public static BalmEntities getEntities() {
        return getRuntime().getEntities();
    }

    public static BalmProviders getProviders() {
        return getRuntime().getProviders();
    }

    public static BalmCommands getCommands() {
        return getRuntime().getCommands();
    }

    public static BalmLootTables getLootTables() {
        return getRuntime().getLootTables();
    }

    public static BalmStats getStats() {
        return getRuntime().getStats();
    }
}
