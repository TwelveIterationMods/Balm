package net.blay09.mods.balm.fabric;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.AbstractBalmConfig;
import net.blay09.mods.balm.api.container.BalmContainerProvider;
import net.blay09.mods.balm.api.entity.BalmEntity;
import net.blay09.mods.balm.api.fluid.BalmFluidTankProvider;
import net.blay09.mods.balm.api.fluid.FluidTank;
import net.blay09.mods.balm.common.command.BalmCommand;
import net.blay09.mods.balm.config.ExampleConfig;
import net.blay09.mods.balm.fabric.fluid.BalmFluidStorage;
import net.blay09.mods.balm.fabric.provider.FabricBalmProviders;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;

public class FabricBalm implements ModInitializer {

    @Override
    public void onInitialize() {
        ((FabricBalmHooks) Balm.getHooks()).initialize();
        ((AbstractBalmConfig) Balm.getConfig()).initialize();
        ExampleConfig.initialize();
        Balm.getCommands().register(BalmCommand::register);

        ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) -> {
            CompoundTag data = ((BalmEntity) oldPlayer).getFabricBalmData();
            ((BalmEntity) newPlayer).setFabricBalmData(data);
        });

        var providers = ((FabricBalmProviders) Balm.getProviders());
        providers.registerProvider(ResourceLocation.fromNamespaceAndPath("balm", "container"), Container.class);
        providers.registerProvider(ResourceLocation.fromNamespaceAndPath("balm", "fluid_tank"), FluidTank.class);

        ItemStorage.SIDED.registerFallback((world, pos, state, blockEntity, direction) -> {
            if (blockEntity instanceof BalmContainerProvider containerProvider) {
                return InventoryStorage.of(containerProvider.getContainer(direction), direction);
            }

            return null;
        });

        FluidStorage.SIDED.registerFallback((world, pos, state, blockEntity, direction) -> {
            if (blockEntity instanceof BalmFluidTankProvider fluidTankProvider) {
                final var fluidTank = fluidTankProvider.getFluidTank(direction);
                if (fluidTank != null) {
                    return new BalmFluidStorage(fluidTank);
                }
            }

            return null;
        });

        Balm.initializeIfLoaded("team_reborn_energy", "net.blay09.mods.balm.fabric.compat.energy.RebornEnergy");
    }
}
