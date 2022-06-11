package net.blay09.mods.balm.forge.world;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.world.BalmWorldGen;
import net.blay09.mods.balm.api.world.BiomePredicate;
import net.blay09.mods.balm.forge.DeferredRegisters;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
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
        public final List<DeferredObject<?>> placementModifiers = new ArrayList<>();

        @SubscribeEvent
        public void commonSetup(FMLCommonSetupEvent event) {
            event.enqueueWork(() -> {
                configuredFeatures.forEach(DeferredObject::resolve);
                placedFeatures.forEach(DeferredObject::resolve);
            });
        }

        @SubscribeEvent
        public void registerFeatures(RegistryEvent.Register<Feature<?>> event) {
            // Technically this is not the right place, but PlacementModifiers aren't a Forge registry yet...
            placementModifiers.forEach(DeferredObject::resolve);
        }
    }

    private final Map<String, Registrations> registrations = new ConcurrentHashMap<>();

    public ForgeBalmWorldGen() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public <T extends Feature<?>> DeferredObject<T> registerFeature(ResourceLocation identifier, Supplier<T> supplier) {
        DeferredRegister<Feature<?>> register = DeferredRegisters.get(ForgeRegistries.FEATURES, identifier.getNamespace());
        RegistryObject<T> registryObject = register.register(identifier.getPath(), supplier);
        return new DeferredObject<>(identifier, registryObject, registryObject::isPresent);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <FC extends FeatureConfiguration, F extends Feature<FC>, T extends ConfiguredFeature<FC, F>> DeferredObject<T> registerConfiguredFeature(ResourceLocation identifier, Supplier<F> featureSupplier, Supplier<FC> configurationSupplier) {
        DeferredObject<T> deferredObject = new DeferredObject<>(identifier, () -> {
            Holder<ConfiguredFeature<FC, ?>> configuredFeature = FeatureUtils.register(identifier.toString(), featureSupplier.get(), configurationSupplier.get());
            return (T) configuredFeature.value();
        });
        getActiveRegistrations().configuredFeatures.add(deferredObject);
        return deferredObject;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends PlacedFeature> DeferredObject<T> registerPlacedFeature(ResourceLocation identifier, Supplier<ConfiguredFeature<?, ?>> configuredFeatureSupplier, PlacementModifier... placementModifiers) {
        DeferredObject<T> deferredObject = new DeferredObject<>(identifier, () -> {
            Holder<PlacedFeature> placedFeature = PlacementUtils.register(identifier.toString(), Holder.direct(configuredFeatureSupplier.get()), placementModifiers);
            return (T) placedFeature.value();
        });
        getActiveRegistrations().placedFeatures.add(deferredObject);
        return deferredObject;
    }

    @Override
    public <T extends PlacementModifierType<?>> DeferredObject<T> registerPlacementModifier(ResourceLocation identifier, Supplier<T> supplier) {
        DeferredObject<T> deferredObject = new DeferredObject<>(identifier, () -> {
            T placementModifierType = supplier.get();
            Registry.register(Registry.PLACEMENT_MODIFIERS, identifier, placementModifierType);
            return placementModifierType;
        });
        getActiveRegistrations().placementModifiers.add(deferredObject);
        return deferredObject;
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
                BuiltinRegistries.PLACED_FEATURE.getHolder(biomeModification.getConfiguredFeatureKey()).ifPresent(placedFeature -> event.getGeneration().addFeature(biomeModification.getStep(), placedFeature));
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
