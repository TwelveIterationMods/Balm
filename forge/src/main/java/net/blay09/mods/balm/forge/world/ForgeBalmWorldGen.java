package net.blay09.mods.balm.forge.world;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.world.BalmWorldGen;
import net.blay09.mods.balm.api.world.BiomePredicate;
import net.blay09.mods.balm.forge.DeferredRegisters;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.ConfiguredDecorator;
import net.minecraft.world.level.levelgen.placement.FeatureDecorator;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class ForgeBalmWorldGen implements BalmWorldGen {

    private static class Registrations {
        public final List<DeferredObject<?>> configuredFeatures = new ArrayList<>();

        @SubscribeEvent
        public void commonSetup(FMLCommonSetupEvent event) {
            configuredFeatures.forEach(DeferredObject::resolve);
        }
    }

    private final Map<String, Registrations> registrations = new ConcurrentHashMap<>();

    public ForgeBalmWorldGen() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public <T extends Feature<?>> DeferredObject<T> registerFeature(Supplier<T> supplier, ResourceLocation identifier) {
        DeferredRegister<Feature<?>> register = DeferredRegisters.get(ForgeRegistries.FEATURES, identifier.getNamespace());
        RegistryObject<T> registryObject = register.register(identifier.getPath(), supplier);
        return new DeferredObject<>(identifier, registryObject, registryObject::isPresent);
    }

    @Override
    public <T extends ConfiguredFeature<?, ?>> DeferredObject<T> registerConfiguredFeature(Supplier<T> supplier, ResourceLocation identifier) {
        DeferredObject<T> deferredObject = new DeferredObject<>(identifier, () -> {
            T configuredFeature = supplier.get();
            Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, identifier, configuredFeature);
            return configuredFeature;
        });
        getActiveRegistrations().configuredFeatures.add(deferredObject);
        return deferredObject;
    }

    @Override
    public <T extends FeatureDecorator<?>> DeferredObject<T> registerDecorator(Supplier<T> supplier, ResourceLocation identifier) {
        DeferredRegister<FeatureDecorator<?>> register = DeferredRegisters.get(ForgeRegistries.DECORATORS, identifier.getNamespace());
        RegistryObject<T> registryObject = register.register(identifier.getPath(), supplier);
        return new DeferredObject<>(identifier, registryObject, registryObject::isPresent);
    }

    @Override
    public <T extends FeatureConfiguration> ConfiguredFeature<?, ?> configuredFeature(Feature<T> feature, T config, ConfiguredDecorator<?> configuredDecorator) {
        return feature
                .configured(config)
                .decorated(configuredDecorator);
    }

    private static final List<BiomeModification> biomeModifications = new ArrayList<>();

    @Override
    public void addFeatureToBiomes(BiomePredicate biomePredicate, GenerationStep.Decoration step, ResourceLocation configuredFeatureIdentifier) {
        ResourceKey<ConfiguredFeature<?, ?>> resourceKey = ResourceKey.create(Registry.CONFIGURED_FEATURE_REGISTRY, configuredFeatureIdentifier);
        biomeModifications.add(new BiomeModification(biomePredicate, step, resourceKey));
    }

    @SubscribeEvent
    public void onBiomeLoading(BiomeLoadingEvent event) {
        for (BiomeModification biomeModification : biomeModifications) {
            if (biomeModification.getBiomePredicate().test(event.getName(), event.getCategory(), event.getClimate().precipitation, event.getClimate().temperature, event.getClimate().downfall)) {
                ConfiguredFeature<?, ?> configuredFeature = BuiltinRegistries.CONFIGURED_FEATURE.get(biomeModification.getConfiguredFeatureKey());
                if (configuredFeature != null) {
                    event.getGeneration().addFeature(biomeModification.getStep(), configuredFeature);
                }
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
