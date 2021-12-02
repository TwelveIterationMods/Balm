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
    <T extends Feature<?>> DeferredObject<T> registerFeature(Supplier<T> supplier, ResourceLocation identifier);

    <T extends ConfiguredFeature<?, ?>> DeferredObject<T> registerConfiguredFeature(Supplier<T> supplier, ResourceLocation identifier);

    <T extends PlacedFeature> DeferredObject<T> registerPlacedFeature(Supplier<T> supplier, ResourceLocation identifier);

    <T extends PlacementModifierType<?>> DeferredObject<T> registerPlacementModifier(Supplier<T> supplier, ResourceLocation identifier);

    default <T extends FeatureConfiguration> ConfiguredFeature<?, ?> configuredFeature(Feature<T> feature, T config) {
        return feature.configured(config);
    }

    default <T extends FeatureConfiguration> PlacedFeature placedFeature(Feature<T> feature, T config, PlacementModifier... placementModifiers) {
        return placedFeature(configuredFeature(feature, config), placementModifiers);
    }

    default PlacedFeature placedFeature(ConfiguredFeature<?, ?> configuredFeature, PlacementModifier... placementModifiers) {
        return configuredFeature.placed(placementModifiers);
    }

    void addFeatureToBiomes(BiomePredicate biomePredicate, GenerationStep.Decoration step, ResourceLocation configuredFeatureIdentifier);
}
