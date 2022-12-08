package net.blay09.mods.balm.forge.item;

import com.mojang.datafixers.util.Pair;
import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.item.BalmItems;
import net.blay09.mods.balm.forge.DeferredRegisters;
import net.blay09.mods.balm.forge.client.rendering.ForgeBalmRenderers;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class ForgeBalmItems implements BalmItems {

    private static class CreativeTabRegistration extends DeferredObject<CreativeModeTab> {
        private final Component displayName;
        private final Supplier<ItemStack> iconSupplier;

        private CreativeTabRegistration(
                ResourceLocation identifier,
                Component displayName,
                Supplier<ItemStack> iconSupplier
        ) {
            super(identifier);
            this.displayName = displayName;
            this.iconSupplier = iconSupplier;
        }

        public void resolve(CreativeModeTabEvent.Register event) {
            set(event.registerCreativeModeTab(getIdentifier(), builder -> builder
                    .title(displayName)
                    .icon(iconSupplier)));
        }
    }

    private static class Registrations {
        public final List<CreativeTabRegistration> creativeTabs = new ArrayList<>();

        @SubscribeEvent
        public void registerCreativeTabs(CreativeModeTabEvent.Register event) {
            creativeTabs.forEach(it -> it.resolve(event));
        }
    }

    private final Map<String, Registrations> registrations = new ConcurrentHashMap<>();

    @Override
    public Item.Properties itemProperties() {
        return new Item.Properties();
    }

    @Override
    public DeferredObject<Item> registerItem(Supplier<Item> supplier, ResourceLocation identifier) {
        DeferredRegister<Item> register = DeferredRegisters.get(ForgeRegistries.ITEMS, identifier.getNamespace());
        RegistryObject<Item> registryObject = register.register(identifier.getPath(), supplier);
        return new DeferredObject<>(identifier, registryObject, registryObject::isPresent);
    }

    @Override
    public DeferredObject<CreativeModeTab> registerCreativeModeTab(ResourceLocation identifier, Supplier<ItemStack> iconSupplier) {
        Component displayName = Component.translatable(identifier.toString().replace(':', '.'));
        CreativeTabRegistration deferred = new CreativeTabRegistration(identifier,
                displayName,
                iconSupplier);
        getActiveRegistrations().creativeTabs.add(deferred);
        return deferred;
    }

    public void register() {
        FMLJavaModLoadingContext.get().getModEventBus().register(getActiveRegistrations());
    }

    private Registrations getActiveRegistrations() {
        return registrations.computeIfAbsent(ModLoadingContext.get().getActiveNamespace(), it -> new Registrations());
    }
}
