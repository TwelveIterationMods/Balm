package net.blay09.mods.balm.forge.world;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.world.BalmWorldGen;
import net.blay09.mods.balm.forge.DeferredRegisters;
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
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ForgeBalmWorldGen implements BalmWorldGen {

    @Override
    public <T extends Feature<?>> DeferredObject<T> registerFeature(Supplier<T> supplier, ResourceLocation identifier) {
        DeferredRegister<Feature<?>> register = DeferredRegisters.get(ForgeRegistries.FEATURES, identifier.getNamespace());
        RegistryObject<T> registryObject = register.register(identifier.getPath(), supplier);
        return new DeferredObject<>(identifier, registryObject);
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
    public <T extends FeatureDecorator<?>> DeferredObject<T> registerDecorator(Supplier<T> supplier, ResourceLocation identifier) {
        DeferredRegister<FeatureDecorator<?>> register = DeferredRegisters.get(ForgeRegistries.DECORATORS, identifier.getNamespace());
        RegistryObject<T> registryObject = register.register(identifier.getPath(), supplier);
        return new DeferredObject<>(identifier, registryObject);
    }

    @Override
    public <T extends FeatureConfiguration> ConfiguredFeature<?, ?> configuredFeature(Feature<T> feature, T config, ConfiguredDecorator<?> configuredDecorator) {
        return feature
                .configured(config)
                .decorated(configuredDecorator);
    }

    private static final List<BiomeModification> biomeModifications = new ArrayList<>();

    @Override
    public void addFeatureToBiomes(Predicate<Biome> biomePredicate, GenerationStep.Decoration step, ResourceLocation configuredFeatureIdentifier) {
        ResourceKey<ConfiguredFeature<?, ?>> resourceKey = ResourceKey.create(Registry.CONFIGURED_FEATURE_REGISTRY, configuredFeatureIdentifier);
        biomeModifications.add(new BiomeModification(biomePredicate, step, resourceKey));
    }

    @SubscribeEvent
    public void onBiomeLoading(BiomeLoadingEvent event) {
        for (BiomeModification biomeModification : biomeModifications) {
            // TODO can't check predicate, as we do not have a Biome yet in Forge. need to refactor that part
            ConfiguredFeature<?, ?> configuredFeature = BuiltinRegistries.CONFIGURED_FEATURE.get(biomeModification.getConfiguredFeatureKey());
            if (configuredFeature != null) {
                event.getGeneration().addFeature(biomeModification.getStep(), configuredFeature);
            }
        }
    }
}
