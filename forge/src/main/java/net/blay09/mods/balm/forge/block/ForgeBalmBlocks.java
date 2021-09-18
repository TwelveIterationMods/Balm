package net.blay09.mods.balm.forge.block;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.block.BalmBlocks;
import net.blay09.mods.balm.forge.DeferredRegisters;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ForgeBalmBlocks implements BalmBlocks {
    @Override
    public BlockBehaviour.Properties blockProperties(Material material) {
        return BlockBehaviour.Properties.of(material);
    }

    @Override
    public Item.Properties itemProperties(CreativeModeTab creativeModeTab) {
        return new Item.Properties().tab(creativeModeTab);
    }

    @Override
    public DeferredObject<Block> registerBlock(Supplier<Block> supplier, ResourceLocation identifier) {
        DeferredRegister<Block> register = DeferredRegisters.get(ForgeRegistries.BLOCKS, identifier.getNamespace());
        RegistryObject<Block> registryObject = register.register(identifier.getPath(), supplier);
        return new DeferredObject<>(identifier, registryObject, registryObject::isPresent);
    }

    @Override
    public DeferredObject<Item> registerBlockItem(Supplier<BlockItem> supplier, ResourceLocation identifier) {
        DeferredRegister<Item> register = DeferredRegisters.get(ForgeRegistries.ITEMS, identifier.getNamespace());
        RegistryObject<Item> registryObject = register.register(identifier.getPath(), supplier);
        return new DeferredObject<>(identifier, registryObject, registryObject::isPresent);
    }

    @Override
    public void register(Supplier<Block> blockSupplier, Supplier<BlockItem> blockItemSupplier, ResourceLocation identifier) {
        registerBlock(blockSupplier, identifier);
        registerBlockItem(blockItemSupplier, identifier);
    }
}
