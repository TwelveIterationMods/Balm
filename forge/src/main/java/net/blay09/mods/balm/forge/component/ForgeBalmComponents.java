package net.blay09.mods.balm.forge.component;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.component.BalmComponents;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class ForgeBalmComponents implements BalmComponents {

    private static class Registrations {
        public final List<DeferredObject<?>> dataComponentTypes = new ArrayList<>();

        @SubscribeEvent
        public void commonSetup(FMLCommonSetupEvent event) {
            event.enqueueWork(() -> dataComponentTypes.forEach(DeferredObject::resolve));
        }
    }

    private final Map<String, ForgeBalmComponents.Registrations> registrations = new ConcurrentHashMap<>();

    @Override
    public <TComponent> DeferredObject<DataComponentType<TComponent>> registerComponent(Supplier<DataComponentType<TComponent>> supplier, ResourceLocation identifier) {
        DeferredObject<DataComponentType<TComponent>> deferredObject = new DeferredObject<>(identifier, () -> {
            DataComponentType<TComponent> dataComponentType = supplier.get();
            Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, identifier, dataComponentType);
            return dataComponentType;
        });
        getActiveRegistrations().dataComponentTypes.add(deferredObject);
        return deferredObject;
    }

    public void register() {
        FMLJavaModLoadingContext.get().getModEventBus().register(getActiveRegistrations());
    }

    private ForgeBalmComponents.Registrations getActiveRegistrations() {
        return registrations.computeIfAbsent(ModLoadingContext.get().getActiveNamespace(), it -> new ForgeBalmComponents.Registrations());
    }
}
