package net.blay09.mods.balm.neoforge;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.AbstractBalmConfig;
import net.blay09.mods.balm.api.energy.EnergyStorage;
import net.blay09.mods.balm.api.fluid.FluidTank;
import net.blay09.mods.balm.config.ExampleConfig;
import net.blay09.mods.balm.neoforge.client.NeoForgeBalmClient;
import net.blay09.mods.balm.neoforge.provider.NeoForgeBalmProviders;
import net.blay09.mods.balm.neoforge.world.NeoForgeBalmWorldGen;
import net.minecraft.world.Container;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.common.capabilities.CapabilityToken;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.items.IItemHandler;

@Mod("balm")
public class NeoForgeBalm {

    public NeoForgeBalm() {
        ((AbstractBalmConfig) Balm.getConfig()).initialize();
        ExampleConfig.initialize();

        NeoForgeBalmWorldGen.initializeBalmBiomeModifiers();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(NeoForgeBalmClient::onInitializeClient);

        NeoForgeBalmProviders providers = (NeoForgeBalmProviders) Balm.getProviders();
        providers.register(IItemHandler.class, new CapabilityToken<>() {
        });
        providers.register(IFluidHandler.class, new CapabilityToken<>() {
        });
        providers.register(IFluidHandlerItem.class, new CapabilityToken<>() {
        });
        providers.register(IEnergyStorage.class, new CapabilityToken<>() {
        });
        providers.register(Container.class, new CapabilityToken<>() {
        });
        providers.register(FluidTank.class, new CapabilityToken<>() {
        });
        providers.register(EnergyStorage.class, new CapabilityToken<>() {
        });
    }

}
