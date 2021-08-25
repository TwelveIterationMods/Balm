package net.blay09.mods.balm.api.item;

import net.blay09.mods.balm.api.DeferredObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public interface BalmItems {
    Item.Properties itemProperties(CreativeModeTab creativeModeTab);

    DeferredObject<Item> registerItem(Supplier<Item> supplier, ResourceLocation identifier);

    CreativeModeTab createCreativeModeTab(ResourceLocation identifier, Supplier<ItemStack> iconSupplier);
}
