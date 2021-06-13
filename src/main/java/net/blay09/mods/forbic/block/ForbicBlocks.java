package net.blay09.mods.forbic.block;

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

public class ForbicBlocks {
    protected static BlockBehaviour.Properties blockProperties(Material material) {
        return FabricBlockSettings.of(material);
    }

    protected static Item.Properties itemProperties(CreativeModeTab creativeModeTab) {
        return new FabricItemSettings().group(creativeModeTab);
    }

    protected static void register(Block block, BlockItem blockItem, ResourceLocation identifier) {
        Registry.register(Registry.BLOCK, identifier, block);
        Registry.register(Registry.ITEM, identifier, blockItem);
    }
}
