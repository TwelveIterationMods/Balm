package net.blay09.mods.balm.fabric.item;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.item.BalmItems;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class FabricBalmItems implements BalmItems {
    @Override
    public Item.Properties itemProperties(CreativeModeTab creativeModeTab) {
        return new FabricItemSettings().group(creativeModeTab);
    }

    @Override
    public DeferredObject<Item> registerItem(Supplier<Item> supplier, ResourceLocation identifier) {
        return new DeferredObject<>(identifier, () -> {
            Item item = supplier.get();
            return Registry.register(Registry.ITEM, identifier, item);
        }).resolveImmediately();
    }

    @Override

    public CreativeModeTab createCreativeModeTab(ResourceLocation identifier, Supplier<ItemStack> iconSupplier) {
        return FabricItemGroupBuilder.build(identifier, iconSupplier);
    }

}
