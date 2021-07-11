package net.blay09.mods.balm.worldgen;

import net.blay09.mods.balm.core.DeferredObject;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.ConfiguredDecorator;
import net.minecraft.world.level.levelgen.placement.FeatureDecorator;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class BalmWorldGen {
    public static <T extends Feature<?>> DeferredObject<T> registerFeature(Supplier<T> supplier, ResourceLocation identifier) {
        return new DeferredObject<>(identifier, () -> {
            T feature = supplier.get();
            Registry.register(Registry.FEATURE, identifier, feature);
            return feature;
        }).resolveImmediately();
    }

    public static <T extends ConfiguredFeature<?, ?>> DeferredObject<T> registerConfiguredFeature(Supplier<T> supplier, ResourceLocation identifier) {
        return new DeferredObject<>(identifier, () -> {
            T configuredFeature = supplier.get();
            Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, identifier, configuredFeature);
            return configuredFeature;
        }).resolveImmediately();
    }

    public static <T extends FeatureDecorator<?>> DeferredObject<T> registerDecorator(Supplier<T> supplier, ResourceLocation identifier) {
        return new DeferredObject<>(identifier, () -> {
            T decorator = supplier.get();
            Registry.register(Registry.DECORATOR, identifier, decorator);
            return decorator;
        }).resolveImmediately();
    }

    public static <T extends FeatureConfiguration> ConfiguredFeature<?, ?> configuredFeature(Feature<T> feature, T config, ConfiguredDecorator<?> configuredDecorator) {
        return feature
                .configured(config)
                .decorated(configuredDecorator);
    }

    public static void addFeatureToBiomes(Predicate<Biome> biomePredicate, GenerationStep.Decoration step, ResourceLocation configuredFeatureIdentifier) {
        BiomeModifications.addFeature(it -> biomePredicate.test(it.getBiome()), step, ResourceKey.create(Registry.CONFIGURED_FEATURE_REGISTRY, configuredFeatureIdentifier));
    }
}
