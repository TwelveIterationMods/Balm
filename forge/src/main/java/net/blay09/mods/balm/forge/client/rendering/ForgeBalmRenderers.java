package net.blay09.mods.balm.forge.client.rendering;

import net.blay09.mods.balm.api.client.rendering.BalmRenderers;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ForgeBalmRenderers implements BalmRenderers {

    private static class ColorRegistration<THandler, TObject> {
        private final THandler color;
        private final TObject[] objects;

        public ColorRegistration(THandler color, TObject[] objects) {
            this.color = color;
            this.objects = objects;
        }

        public THandler getColor() {
            return color;
        }

        public TObject[] getObjects() {
            return objects;
        }
    }

    private final Map<ModelLayerLocation, Supplier<LayerDefinition>> layerDefinitions = new HashMap<>();
    private final List<ColorRegistration<BlockColor, Block>> blockColors = new ArrayList<>();
    private final List<ColorRegistration<ItemColor, ItemLike>> itemColors = new ArrayList<>();

    public ForgeBalmRenderers() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public ModelLayerLocation registerModel(ResourceLocation location, Supplier<LayerDefinition> layerDefinition) {
        ModelLayerLocation modelLayerLocation = new ModelLayerLocation(location, "main");
        layerDefinitions.put(modelLayerLocation, layerDefinition);
        return modelLayerLocation;
    }

    @Override
    public Map<ModelLayerLocation, LayerDefinition> createRoots() {
        return layerDefinitions.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, it -> it.getValue().get()));
    }

    @Override
    public <T extends BlockEntity> void registerBlockEntityRenderer(BlockEntityType<T> type, BlockEntityRendererProvider<? super T> provider) {
        BlockEntityRenderers.register(type, provider);
    }

    @Override
    public void registerBlockColorHandler(BlockColor color, Block... blocks) {
        blockColors.add(new ColorRegistration<>(color, blocks));
    }

    @Override
    public void registerItemColorHandler(ItemColor color, ItemLike... items) {
        itemColors.add(new ColorRegistration<>(color, items));
    }

    @Override
    public void setBlockRenderType(Block block, RenderType renderType) {
        ItemBlockRenderTypes.setRenderLayer(block, renderType);
    }

    @SubscribeEvent
    public void initBlockColors(ColorHandlerEvent.Block event) {
        for (ColorRegistration<BlockColor, Block> blockColor : blockColors) {
            event.getBlockColors().register(blockColor.getColor(), blockColor.getObjects());
        }
    }

    @SubscribeEvent
    public void initItemColors(ColorHandlerEvent.Item event) {
        for (ColorRegistration<ItemColor, ItemLike> itemColor : itemColors) {
            event.getItemColors().register(itemColor.getColor(), itemColor.getObjects());
        }
    }
}
