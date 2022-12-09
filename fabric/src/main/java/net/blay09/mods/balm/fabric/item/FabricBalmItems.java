package net.blay09.mods.balm.fabric.item;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.item.BalmItems;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class FabricBalmItems implements BalmItems {
    @Override
    public Item.Properties itemProperties() {
        return new FabricItemSettings();
    }

    @Override
    public DeferredObject<Item> registerItem(Supplier<Item> supplier, ResourceLocation identifier) {
        return new DeferredObject<>(identifier, () -> {
            Item item = supplier.get();
            return Registry.register(BuiltInRegistries.ITEM, identifier, item);
        }).resolveImmediately();
    }


    @Override
    public DeferredObject<CreativeModeTab> registerCreativeModeTab(ResourceLocation identifier, Supplier<ItemStack> iconSupplier) {
        return new DeferredObject<>(identifier, () -> {
            Component displayName = Component.translatable("itemGroup." + identifier.toString().replace(':', '.'));
            return FabricItemGroup.builder(identifier)
                    .title(displayName)
                    .icon(iconSupplier)
                    .build();
        }).resolveImmediately();
    }

}
