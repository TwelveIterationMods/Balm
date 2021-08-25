package net.blay09.mods.balm.api.block;

import net.blay09.mods.balm.api.DeferredObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

import java.util.function.Supplier;

public interface BalmBlocks {
    BlockBehaviour.Properties blockProperties(Material material);
    Item.Properties itemProperties(CreativeModeTab creativeModeTab);
    DeferredObject<Block> registerBlock(Supplier<Block> supplier, ResourceLocation identifier);
    DeferredObject<Item> registerBlockItem(Supplier<BlockItem> supplier, ResourceLocation identifier);
    void register(Supplier<Block> blockSupplier, Supplier<BlockItem> blockItemSupplier, ResourceLocation identifier);
}
