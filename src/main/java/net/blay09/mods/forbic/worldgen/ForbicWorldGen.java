package net.blay09.mods.forbic.worldgen;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.ConfiguredDecorator;
import net.minecraft.world.level.levelgen.placement.FeatureDecorator;

public class ForbicWorldGen {
    protected static <T extends Feature<?>> T registerFeature(T feature, ResourceLocation identifier) {
        // TODO
        return feature;
    }

    protected static <T extends ConfiguredFeature<?, ?>> T registerConfiguredFeature(T configuredFeature, ResourceLocation identifier) {
        // TODO Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, identifier, configuredFeature);
        return configuredFeature;
    }

    protected static <T extends FeatureDecorator<?>> T registerDecorator(T decorator, ResourceLocation identifier) {
        // TODO
        return decorator;
    }

    protected static <T extends FeatureConfiguration> ConfiguredFeature<?, ?> configuredFeature(Feature<T> feature, T config, ConfiguredDecorator<?> configuredDecorator) {
        return feature
                .configured(config)
                .decorated(configuredDecorator);
    }
}
