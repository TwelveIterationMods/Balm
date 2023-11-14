package net.blay09.mods.balm.neoforge.block;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.neoforge.DeferredRegisters;
import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.block.BalmBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.ForgeRegistries;
import net.neoforged.neoforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class NeoForgeBalmBlocks implements BalmBlocks {
    @Override
    public BlockBehaviour.Properties blockProperties() {
        return BlockBehaviour.Properties.of();
    }

    @Override
    public DeferredObject<Block> registerBlock(Supplier<Block> supplier, ResourceLocation identifier) {
        DeferredRegister<Block> register = DeferredRegisters.get(ForgeRegistries.BLOCKS, identifier.getNamespace());
        RegistryObject<Block> registryObject = register.register(identifier.getPath(), supplier);
        return new DeferredObject<>(identifier, registryObject, registryObject::isPresent);
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
