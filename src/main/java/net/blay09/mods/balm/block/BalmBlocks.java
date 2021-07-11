package net.blay09.mods.balm.block;

import net.blay09.mods.balm.core.DeferredObject;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

import java.util.function.Supplier;

public class BalmBlocks {
    public static BlockBehaviour.Properties blockProperties(Material material) {
        return FabricBlockSettings.of(material);
    }

    public static Item.Properties itemProperties(CreativeModeTab creativeModeTab) {
        return new FabricItemSettings().group(creativeModeTab);
    }

    public static DeferredObject<Block> registerBlock(Supplier<Block> supplier, ResourceLocation identifier) {
        return new DeferredObject<>(identifier, () -> {
            Block block = supplier.get();
            return Registry.register(Registry.BLOCK, identifier, block);
        }).resolveImmediately();
    }

    public static DeferredObject<Item> registerBlockItem(Supplier<BlockItem> supplier, ResourceLocation identifier) {
        return new DeferredObject<>(identifier, () -> {
            Item item = supplier.get();
            return Registry.register(Registry.ITEM, identifier, item);
        }).resolveImmediately();
    }

    public static void register(Supplier<Block> blockSupplier, Supplier<BlockItem> blockItemSupplier, ResourceLocation identifier) {
        registerBlock(blockSupplier, identifier);
        registerBlockItem(blockItemSupplier, identifier);
    }
}
