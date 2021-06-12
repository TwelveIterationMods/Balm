package net.blay09.mods.forbic;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class ForbicItems {
    protected static Item.Properties itemProperties(CreativeModeTab creativeModeTab) {
        return new FabricItemSettings().group(creativeModeTab);
    }

    protected static void register(Item item, ResourceLocation identifier) {
        Registry.register(Registry.ITEM, identifier, item);
    }

    protected static CreativeModeTab createCreativeModeTab(ResourceLocation identifier, Supplier<ItemStack> iconSupplier) {
        return FabricItemGroupBuilder.build(identifier, iconSupplier);
    }
}
