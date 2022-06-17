package net.blay09.mods.balm.fabric.client.rendering;

import com.mojang.math.Matrix4f;
import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.client.rendering.BalmModels;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class FabricBalmModels implements BalmModels { // TODO Models
    @Override
    public DeferredObject<BakedModel> loadModel(ResourceLocation identifier) {
        return new DeferredObject<>(identifier) {
            @Override
            public BakedModel resolve() {
                return Minecraft.getInstance().getBlockRenderer().getBlockModel(Blocks.DIRT.defaultBlockState());
            }
        };
    }

    @Override
    public DeferredObject<BakedModel> bakeModel(ResourceLocation identifier, UnbakedModel model) {
        return new DeferredObject<>(identifier) {
            @Override
            public BakedModel resolve() {
                return Minecraft.getInstance().getBlockRenderer().getBlockModel(Blocks.DIRT.defaultBlockState());
            }
        };
    }

    @Override
    public DeferredObject<BakedModel> loadDynamicModel(ResourceLocation identifier, @Nullable Function<BlockState, ResourceLocation> modelFunction, @Nullable Function<BlockState, Map<String, String>> textureMapFunction, @Nullable BiConsumer<BlockState, Matrix4f> transformFunction) {
        return new DeferredObject<>(identifier) {
            @Override
            public BakedModel resolve() {
                return Minecraft.getInstance().getBlockRenderer().getBlockModel(Blocks.DIRT.defaultBlockState());
            }
        };
    }

    @Override
    public DeferredObject<BakedModel> retexture(ResourceLocation identifier, Map<String, String> textureMap) {
        return new DeferredObject<>(identifier) {
            @Override
            public BakedModel resolve() {
                return Minecraft.getInstance().getBlockRenderer().getBlockModel(Blocks.DIRT.defaultBlockState());
            }
        };
    }

    @Override
    public void overrideModel(Supplier<Block> block, Supplier<BakedModel> model) {
    }
}
