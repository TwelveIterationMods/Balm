package net.blay09.mods.balm.fabric.block;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.block.BalmBlocks;
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

public class FabricBalmBlocks implements BalmBlocks {
    @Override
    public BlockBehaviour.Properties blockProperties(Material material) {
        return FabricBlockSettings.of(material);
    }

    @Override
    public Item.Properties itemProperties(CreativeModeTab creativeModeTab) {
        return new FabricItemSettings().group(creativeModeTab);
    }

    @Override
    public DeferredObject<Block> registerBlock(Supplier<Block> supplier, ResourceLocation identifier) {
        return new DeferredObject<>(identifier, () -> {
            Block block = supplier.get();
            return Registry.register(Registry.BLOCK, identifier, block);
        }).resolveImmediately();
    }

    @Override
    public DeferredObject<Item> registerBlockItem(Supplier<BlockItem> supplier, ResourceLocation identifier) {
        return new DeferredObject<>(identifier, () -> {
            Item item = supplier.get();
            return Registry.register(Registry.ITEM, identifier, item);
        }).resolveImmediately();
    }

    @Override
    public void register(Supplier<Block> blockSupplier, Supplier<BlockItem> blockItemSupplier, ResourceLocation identifier) {
        registerBlock(blockSupplier, identifier);
        registerBlockItem(blockItemSupplier, identifier);
    }
}
