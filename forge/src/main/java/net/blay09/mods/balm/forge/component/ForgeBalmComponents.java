package net.blay09.mods.balm.forge.component;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.component.BalmComponents;
import net.blay09.mods.balm.forge.DeferredRegisters;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class ForgeBalmComponents implements BalmComponents {

    @Override
    public <TComponent> DeferredObject<DataComponentType<TComponent>> registerComponent(Supplier<DataComponentType<TComponent>> supplier, ResourceLocation identifier) {
        final var register = DeferredRegisters.get(Registries.DATA_COMPONENT_TYPE, identifier.getNamespace());
        final var registryObject = register.register(identifier.getPath(), supplier);
        return new DeferredObject<>(identifier, registryObject, registryObject::isPresent);
    }

}
