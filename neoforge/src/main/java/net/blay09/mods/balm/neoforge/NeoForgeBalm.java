package net.blay09.mods.balm.neoforge;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.AbstractBalmConfig;
import net.blay09.mods.balm.config.ExampleConfig;
import net.blay09.mods.balm.neoforge.client.NeoForgeBalmClient;
import net.blay09.mods.balm.neoforge.provider.NeoForgeBalmProviders;
import net.blay09.mods.balm.neoforge.world.NeoForgeBalmWorldGen;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.items.IItemHandler;

@Mod("balm")
public class NeoForgeBalm {

    public NeoForgeBalm(IEventBus modBus) {
        ((AbstractBalmConfig) Balm.getConfig()).initialize();
        ExampleConfig.initialize();

        NeoForgeBalmWorldGen.initializeBalmBiomeModifiers();
        modBus.addListener(NeoForgeBalmClient::onInitializeClient);

        NeoForgeBalmProviders providers = (NeoForgeBalmProviders) Balm.getProviders();
        providers.registerBlockProvider(IItemHandler.class, Capabilities.ItemHandler.BLOCK);
        providers.registerBlockProvider(IFluidHandler.class, Capabilities.FluidHandler.BLOCK);
        providers.registerItemProvider(IFluidHandlerItem.class, Capabilities.FluidHandler.ITEM);
        providers.registerBlockProvider(IEnergyStorage.class, Capabilities.EnergyStorage.BLOCK);
    }

}
