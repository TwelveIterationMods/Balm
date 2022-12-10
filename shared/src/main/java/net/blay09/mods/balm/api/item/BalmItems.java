package net.blay09.mods.balm.api.item;

import net.blay09.mods.balm.api.DeferredObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public interface BalmItems {
    Item.Properties itemProperties();

    default DeferredObject<Item> registerItem(Supplier<Item> supplier, ResourceLocation identifier) {
        return registerItem(supplier, identifier, identifier.withPath(identifier.getNamespace()));
    }

    DeferredObject<Item> registerItem(Supplier<Item> supplier, ResourceLocation identifier, @Nullable ResourceLocation creativeTab);

    DeferredObject<CreativeModeTab> registerCreativeModeTab(ResourceLocation identifier, Supplier<ItemStack> iconSupplier);

    void addToCreativeModeTab(ResourceLocation tabIdentifier, Supplier<ItemLike[]> itemsSupplier);
}
