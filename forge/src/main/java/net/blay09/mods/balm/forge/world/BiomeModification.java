package net.blay09.mods.balm.forge.world;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import java.util.function.Predicate;

class BiomeModification {
    private final Predicate<Biome> biomePredicate;
    private final GenerationStep.Decoration step;
    private final ResourceKey<ConfiguredFeature<?, ?>> configuredFeatureKey;

    BiomeModification(Predicate<Biome> biomePredicate, GenerationStep.Decoration step, ResourceKey<ConfiguredFeature<?, ?>> configuredFeatureKey) {
        this.biomePredicate = biomePredicate;
        this.step = step;
        this.configuredFeatureKey = configuredFeatureKey;
    }

    public Predicate<Biome> getBiomePredicate() {
        return biomePredicate;
    }

    public GenerationStep.Decoration getStep() {
        return step;
    }

    public ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeatureKey() {
        return configuredFeatureKey;
    }
}
