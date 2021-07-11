package net.blay09.mods.balm.item;

import net.blay09.mods.balm.core.DeferredObject;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class BalmItems {
    public static Item.Properties itemProperties(CreativeModeTab creativeModeTab) {
        return new FabricItemSettings().group(creativeModeTab);
    }

    public static DeferredObject<Item> registerItem(Supplier<Item> supplier, ResourceLocation identifier) {
        return new DeferredObject<>(identifier, () -> {
            Item item = supplier.get();
            return Registry.register(Registry.ITEM, identifier, item);
        }).resolveImmediately();
    }

    public static CreativeModeTab createCreativeModeTab(ResourceLocation identifier, Supplier<ItemStack> iconSupplier) {
        return FabricItemGroupBuilder.build(identifier, iconSupplier);
    }

    public static ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        return new ItemStack(itemStack.getItem().getCraftingRemainingItem());
    }
}
