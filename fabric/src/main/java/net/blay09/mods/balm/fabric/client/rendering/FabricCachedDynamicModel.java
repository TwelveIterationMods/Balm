package net.blay09.mods.balm.fabric.client.rendering;

import net.blay09.mods.balm.common.client.rendering.AbstractCachedDynamicModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class FabricCachedDynamicModel extends AbstractCachedDynamicModel {
    public FabricCachedDynamicModel(ModelBakery modelBakery, Function<BlockState, ResourceLocation> baseModelFunction, @Nullable List<Pair<Predicate<BlockState>, BakedModel>> parts, @Nullable Function<BlockState, Map<String, String>> textureMapFunction, @Nullable BiConsumer<BlockState, Matrix4f> transformFunction, List<RenderType> renderTypes, ResourceLocation location) {
        super(modelBakery, baseModelFunction, parts, textureMapFunction, transformFunction, renderTypes, location);
    }

    @Override
    public List<RenderType> getBlockRenderTypes(BlockState state, RandomSource rand) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<RenderType> getItemRenderTypes(ItemStack itemStack, boolean fabulous) {
        throw new UnsupportedOperationException();
    }
}
