package net.blay09.mods.balm.api;

import net.blay09.mods.balm.api.block.BalmBlockEntities;
import net.blay09.mods.balm.api.block.BalmBlocks;
import net.blay09.mods.balm.api.config.BalmConfig;
import net.blay09.mods.balm.api.event.BalmEvents;
import net.blay09.mods.balm.api.item.BalmItems;
import net.blay09.mods.balm.api.menu.BalmMenus;
import net.blay09.mods.balm.api.network.BalmNetworking;
import net.blay09.mods.balm.api.world.BalmWorldGen;

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
    boolean isModLoaded(String modId);
}
