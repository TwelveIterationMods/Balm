package net.blay09.mods.balm.forge.world;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.world.BalmWorldGen;
import net.blay09.mods.balm.api.world.BiomePredicate;
import net.blay09.mods.balm.forge.DeferredRegisters;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class ForgeBalmWorldGen implements BalmWorldGen {

    private static class Registrations {
        public final List<DeferredObject<?>> configuredFeatures = new ArrayList<>();
        public final List<DeferredObject<?>> placedFeatures = new ArrayList<>();

        @SubscribeEvent
        public void commonSetup(FMLCommonSetupEvent event) {
            configuredFeatures.forEach(DeferredObject::resolve);
            placedFeatures.forEach(DeferredObject::resolve);
        }
    }

    private final Map<String, Registrations> registrations = new ConcurrentHashMap<>();

    public ForgeBalmWorldGen() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public <T extends Feature<?>> DeferredObject<T> registerFeature(Supplier<T> supplier, ResourceLocation identifier) {
        DeferredRegister<Feature<?>> register = DeferredRegisters.get(ForgeRegistries.FEATURES, identifier.getNamespace());
        RegistryObject<T> registryObject = register.register(identifier.getPath(), supplier);
        return new DeferredObject<>(identifier, registryObject, registryObject::isPresent);
    }

    @Override
    public <T extends ConfiguredFeature<?, ?>> DeferredObject<T> registerConfiguredFeature(Supplier<T> supplier, ResourceLocation identifier) {
        DeferredObject<T> deferredObject = new DeferredObject<>(identifier, () -> {
            T configuredFeature = supplier.get();
            Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, identifier, configuredFeature);
            return configuredFeature;
        });
        getActiveRegistrations().configuredFeatures.add(deferredObject);
        return deferredObject;
    }

    @Override
    public <T extends PlacedFeature> DeferredObject<T> registerPlacedFeature(Supplier<T> supplier, ResourceLocation identifier) {
        DeferredObject<T> deferredObject = new DeferredObject<>(identifier, () -> {
            T placedFeature = supplier.get();
            Registry.register(BuiltinRegistries.PLACED_FEATURE, identifier, placedFeature);
            return placedFeature;
        });
        getActiveRegistrations().placedFeatures.add(deferredObject);
        return deferredObject;
    }

    @Override
    public <T extends PlacementModifierType<?>> DeferredObject<T> registerPlacementModifier(Supplier<T> supplier, ResourceLocation identifier) {
        // TODO 1.18 bet this will break horribly but placement modifiers aren't a Forge registry yet
        return new DeferredObject<>(identifier, () -> {
            T placementModifierType = supplier.get();
            Registry.register(Registry.PLACEMENT_MODIFIERS, identifier, placementModifierType);
            return placementModifierType;
        }).resolveImmediately();
    }

    private static final List<BiomeModification> biomeModifications = new ArrayList<>();

    @Override
    public void addFeatureToBiomes(BiomePredicate biomePredicate, GenerationStep.Decoration step, ResourceLocation placedFeatureIdentifier) {
        ResourceKey<PlacedFeature> resourceKey = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, placedFeatureIdentifier);
        biomeModifications.add(new BiomeModification(biomePredicate, step, resourceKey));
    }

    @SubscribeEvent
    public void onBiomeLoading(BiomeLoadingEvent event) {
        for (BiomeModification biomeModification : biomeModifications) {
            if (biomeModification.getBiomePredicate().test(event.getName(), event.getCategory(), event.getClimate().precipitation, event.getClimate().temperature, event.getClimate().downfall)) {
                PlacedFeature placedFeature = BuiltinRegistries.PLACED_FEATURE.get(biomeModification.getConfiguredFeatureKey());
                if (placedFeature != null) {
                    event.getGeneration().addFeature(biomeModification.getStep(), placedFeature);
                }
            }
        }
    }

    public void register() {
        FMLJavaModLoadingContext.get().getModEventBus().register(getActiveRegistrations());
    }

    private Registrations getActiveRegistrations() {
        return registrations.computeIfAbsent(ModLoadingContext.get().getActiveNamespace(), it -> new Registrations());
    }
}
