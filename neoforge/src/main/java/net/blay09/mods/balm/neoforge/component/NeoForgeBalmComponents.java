package net.blay09.mods.balm.neoforge.component;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.component.BalmComponents;
import net.blay09.mods.balm.neoforge.DeferredRegisters;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class NeoForgeBalmComponents implements BalmComponents {
    @Override
    public <TComponent> DeferredObject<DataComponentType<TComponent>> registerComponent(Supplier<DataComponentType<TComponent>> supplier, ResourceLocation identifier) {
        final var register = DeferredRegisters.get(Registries.DATA_COMPONENT_TYPE, identifier.getNamespace());
        final var registryObject = register.register(identifier.getPath(), supplier);
        return new DeferredObject<>(identifier, registryObject, registryObject::isBound);
    }
}
