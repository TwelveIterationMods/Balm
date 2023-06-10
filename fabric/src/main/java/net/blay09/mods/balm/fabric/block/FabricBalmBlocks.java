package net.blay09.mods.balm.fabric.block;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.block.BalmBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class FabricBalmBlocks implements BalmBlocks {
    @Override
    public BlockBehaviour.Properties blockProperties() {
        return FabricBlockSettings.create();
    }

    @Override
    public DeferredObject<Block> registerBlock(Supplier<Block> supplier, ResourceLocation identifier) {
        return new DeferredObject<>(identifier, () -> {
            Block block = supplier.get();
            return Registry.register(BuiltInRegistries.BLOCK, identifier, block);
        }).resolveImmediately();
    }

    @Override
    public DeferredObject<Item> registerBlockItem(Supplier<BlockItem> supplier, ResourceLocation identifier, @Nullable ResourceLocation creativeTab) {
        return Balm.getItems().registerItem(supplier::get, identifier, creativeTab);
    }

    @Override
    public void register(Supplier<Block> blockSupplier, Supplier<BlockItem> blockItemSupplier, ResourceLocation identifier, @Nullable ResourceLocation creativeTab) {
        registerBlock(blockSupplier, identifier);
        registerBlockItem(blockItemSupplier, identifier, creativeTab);
    }
}
