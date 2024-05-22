package net.blay09.mods.balm.api.component;

import net.blay09.mods.balm.api.DeferredObject;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public interface BalmComponents {
    <TComponent> DeferredObject<DataComponentType<TComponent>> registerComponent(Supplier<DataComponentType<TComponent>> supplier, ResourceLocation identifier);
}
