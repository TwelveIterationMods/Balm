package net.blay09.mods.forbic.worldgen;

import net.blay09.mods.forbic.core.DeferredObject;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.ConfiguredDecorator;
import net.minecraft.world.level.levelgen.placement.FeatureDecorator;

import java.util.function.Supplier;

public class ForbicWorldGen {
    protected static <T extends Feature<?>> DeferredObject<T> registerFeature(Supplier<T> supplier, ResourceLocation identifier) {
        return new DeferredObject<>(() -> {
            T feature = supplier.get();
            Registry.register(Registry.FEATURE, identifier, feature);
            return feature;
        }).resolveImmediately();
    }

    protected static <T extends ConfiguredFeature<?, ?>> DeferredObject<T> registerConfiguredFeature(Supplier<T> supplier, ResourceLocation identifier) {
        return new DeferredObject<>(() -> {
            T configuredFeature = supplier.get();
            Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, identifier, configuredFeature);
            return configuredFeature;
        }).resolveImmediately();
    }

    protected static <T extends FeatureDecorator<?>> DeferredObject<T> registerDecorator(Supplier<T> supplier, ResourceLocation identifier) {
        return new DeferredObject<>(() -> {
            T decorator = supplier.get();
            Registry.register(Registry.DECORATOR, identifier, decorator);
            return decorator;
        }).resolveImmediately();
    }

    protected static <T extends FeatureConfiguration> ConfiguredFeature<?, ?> configuredFeature(Feature<T> feature, T config, ConfiguredDecorator<?> configuredDecorator) {
        return feature
                .configured(config)
                .decorated(configuredDecorator);
    }
}
