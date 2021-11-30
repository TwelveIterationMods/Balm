package net.blay09.mods.balm.fabric.world;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.world.BalmWorldGen;
import net.blay09.mods.balm.api.world.BiomePredicate;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

import java.util.function.Supplier;

public class FabricBalmWorldGen implements BalmWorldGen {
    @Override
    public <T extends Feature<?>> DeferredObject<T> registerFeature(Supplier<T> supplier, ResourceLocation identifier) {
        return new DeferredObject<>(identifier, () -> {
            T feature = supplier.get();
            Registry.register(Registry.FEATURE, identifier, feature);
            return feature;
        }).resolveImmediately();
    }

    @Override
    public <T extends ConfiguredFeature<?, ?>> DeferredObject<T> registerConfiguredFeature(Supplier<T> supplier, ResourceLocation identifier) {
        return new DeferredObject<>(identifier, () -> {
            T configuredFeature = supplier.get();
            Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, identifier, configuredFeature);
            return configuredFeature;
        }).resolveImmediately();
    }

    @Override
    public <T extends PlacementModifierType<?>> DeferredObject<T> registerPlacementModifier(Supplier<T> supplier, ResourceLocation identifier) {
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
                it.getBiome().getBiomeCategory(),
                it.getBiome().getPrecipitation(),
                it.getBiome().getBaseTemperature(),
                it.getBiome().getDownfall()), step, ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, placedFeatureIdentifier));
    }
}
