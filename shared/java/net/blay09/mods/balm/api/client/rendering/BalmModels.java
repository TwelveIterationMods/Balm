package net.blay09.mods.balm.api.client.rendering;

import com.mojang.math.Matrix4f;
import net.blay09.mods.balm.api.DeferredObject;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface BalmModels {
    DeferredObject<BakedModel> loadModel(ResourceLocation identifier);

    DeferredObject<BakedModel> bakeModel(ResourceLocation identifier, UnbakedModel model);

    DeferredObject<BakedModel> loadDynamicModel(ResourceLocation identifier, @Nullable Function<BlockState, ResourceLocation> modelFunction, @Nullable Function<BlockState, Map<String, String>> textureMapFunction, @Nullable BiConsumer<BlockState, Matrix4f> transformFunction);

    DeferredObject<BakedModel> retexture(ResourceLocation identifier, Map<String, String> textureMap);

    default void overrideModel(Block block, Supplier<BakedModel> model) {
        overrideModel(() -> block, model);
    }

    void overrideModel(Supplier<Block> block, Supplier<BakedModel> model);
}
