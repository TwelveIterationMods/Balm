package net.blay09.mods.balm.api.client.rendering;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Map;
import java.util.function.Supplier;

public interface BalmRenderers {
    ModelLayerLocation registerModel(ResourceLocation location, Supplier<LayerDefinition> layerDefinition);

    <T extends BlockEntity> void registerBlockEntityRenderer(BlockEntityType<T> type, BlockEntityRendererProvider<? super T> provider);

    void registerBlockColorHandler(BlockColor color, Block... blocks);

    void registerItemColorHandler(ItemColor color, ItemLike... items);

    void setBlockRenderType(Block block, RenderType renderType);
}
