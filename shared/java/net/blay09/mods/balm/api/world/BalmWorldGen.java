package net.blay09.mods.balm.api.world;

import net.blay09.mods.balm.api.DeferredObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

import java.util.function.Supplier;

public interface BalmWorldGen {
    <T extends Feature<?>> DeferredObject<T> registerFeature(ResourceLocation identifier, Supplier<T> supplier);

    <FC extends FeatureConfiguration, F extends Feature<FC>, T extends ConfiguredFeature<FC, F>> DeferredObject<T> registerConfiguredFeature(ResourceLocation identifier, Supplier<F> featureSupplier, Supplier<FC> configurationSupplier);

    <T extends PlacedFeature> DeferredObject<T> registerPlacedFeature(ResourceLocation identifier, Supplier<ConfiguredFeature<?, ?>> configuredFeatureSupplier, PlacementModifier... placementModifiers);

    <T extends PlacementModifierType<?>> DeferredObject<T> registerPlacementModifier(ResourceLocation identifier, Supplier<T> supplier);

    void addFeatureToBiomes(BiomePredicate biomePredicate, GenerationStep.Decoration step, ResourceLocation configuredFeatureIdentifier);
}
