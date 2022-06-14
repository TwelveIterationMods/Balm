package net.blay09.mods.balm.fabric.world;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.world.BalmWorldGen;
import net.blay09.mods.balm.api.world.BiomePredicate;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
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

import java.util.function.Supplier;

public class FabricBalmWorldGen implements BalmWorldGen {
    @Override
    public <T extends Feature<?>> DeferredObject<T> registerFeature(ResourceLocation identifier, Supplier<T> supplier) {
        return new DeferredObject<>(identifier, () -> {
            T feature = supplier.get();
            Registry.register(Registry.FEATURE, identifier, feature);
            return feature;
        }).resolveImmediately();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <FC extends FeatureConfiguration, F extends Feature<FC>, T extends ConfiguredFeature<FC, F>> DeferredObject<T> registerConfiguredFeature(ResourceLocation identifier, Supplier<F> featureSupplier, Supplier<FC> configurationSupplier){
        return new DeferredObject<>(identifier, () -> {
            Holder<ConfiguredFeature<FC, ?>> configuredFeature = FeatureUtils.register(identifier.toString(), featureSupplier.get(), configurationSupplier.get());
            return (T) configuredFeature.value();
        }).resolveImmediately();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends PlacedFeature> DeferredObject<T> registerPlacedFeature(ResourceLocation identifier, Supplier<ConfiguredFeature<?, ?>> configuredFeatureSupplier, PlacementModifier... placementModifiers) {
        return new DeferredObject<>(identifier, () -> {
            Holder<PlacedFeature> placedFeature = PlacementUtils.register(identifier.toString(), Holder.direct(configuredFeatureSupplier.get()), placementModifiers);
            return (T) placedFeature.value();
        }).resolveImmediately();
    }

    @Override
    public <T extends PlacementModifierType<?>> DeferredObject<T> registerPlacementModifier(ResourceLocation identifier, Supplier<T> supplier) {
        return new DeferredObject<>(identifier, () -> {
            T placementModifierType = supplier.get();
            Registry.register(Registry.PLACEMENT_MODIFIERS, identifier, placementModifierType);
            return placementModifierType;
        }).resolveImmediately();
    }

    @Override
    public void addFeatureToBiomes(BiomePredicate biomePredicate, GenerationStep.Decoration step, ResourceLocation placedFeatureIdentifier) {
        BiomeModifications.addFeature(it -> biomePredicate.test(
                it.getBiomeKey().location(),
                it.getBiome().getPrecipitation(),
                it.getBiome().getBaseTemperature(),
                it.getBiome().getDownfall()), step, ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, placedFeatureIdentifier));
    }
}
