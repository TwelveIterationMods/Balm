package net.blay09.mods.balm.forge;

import net.blay09.mods.balm.api.BalmHooks;
import net.blay09.mods.balm.api.BalmRuntime;
import net.blay09.mods.balm.api.block.BalmBlockEntities;
import net.blay09.mods.balm.api.block.BalmBlocks;
import net.blay09.mods.balm.api.client.BalmClient;
import net.blay09.mods.balm.api.config.BalmConfig;
import net.blay09.mods.balm.api.event.BalmEvents;
import net.blay09.mods.balm.api.event.ForgeBalmEvents;
import net.blay09.mods.balm.api.item.BalmItems;
import net.blay09.mods.balm.api.menu.BalmMenus;
import net.blay09.mods.balm.api.network.BalmNetworking;
import net.blay09.mods.balm.api.world.BalmWorldGen;
import net.blay09.mods.balm.forge.block.ForgeBalmBlocks;
import net.blay09.mods.balm.forge.block.entity.ForgeBalmBlockEntities;
import net.blay09.mods.balm.forge.config.ForgeBalmConfig;
import net.blay09.mods.balm.forge.event.ForgeBalmCommonEvents;
import net.blay09.mods.balm.forge.item.ForgeBalmItems;
import net.blay09.mods.balm.forge.menu.ForgeBalmMenus;
import net.blay09.mods.balm.forge.network.ForgeBalmNetworking;
import net.blay09.mods.balm.forge.world.ForgeBalmWorldGen;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;

public class ForgeBalmRuntime implements BalmRuntime {
    private final BalmWorldGen worldGen = new ForgeBalmWorldGen();
    private final BalmBlocks blocks = new ForgeBalmBlocks();
    private final BalmBlockEntities blockEntities = new ForgeBalmBlockEntities();
    private final ForgeBalmEvents events = new ForgeBalmEvents();
    private final BalmItems items = new ForgeBalmItems();
    private final BalmMenus menus = new ForgeBalmMenus();
    private final BalmNetworking networking = new ForgeBalmNetworking();
    private final BalmConfig config = new ForgeBalmConfig();
    private final BalmHooks hooks = new ForgeBalmHooks();

    public ForgeBalmRuntime() {
        ForgeBalmCommonEvents.registerEvents(events);
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
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public void initialize(String modId) {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        for (DeferredRegister<?> deferredRegister : DeferredRegisters.getByModId(modId)) {
            deferredRegister.register(modEventBus);
        }
    }
}
