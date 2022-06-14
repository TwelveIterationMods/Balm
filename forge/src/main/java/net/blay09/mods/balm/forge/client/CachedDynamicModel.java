package net.blay09.mods.balm.forge.client;

import com.mojang.math.Matrix4f;
import com.mojang.math.Transformation;
import net.blay09.mods.balm.forge.client.rendering.ForgeBalmModels;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.ForgeModelBakery;
import net.minecraftforge.client.model.SimpleModelState;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class CachedDynamicModel implements BakedModel {

    private final Map<String, BakedModel> cache = new HashMap<>();
    private final Map<ResourceLocation, BakedModel> baseModelCache = new HashMap<>();

    private final ModelBakery modelBakery;
    private final Function<BlockState, ResourceLocation> baseModelFunction;
    private final List<Pair<Predicate<BlockState>, BakedModel>> parts;
    private final Function<BlockState, Map<String, String>> textureMapFunction;
    private final BiConsumer<BlockState, Matrix4f> transformFunction;
    private final ResourceLocation location;

    private TextureAtlasSprite particleTexture;

    public CachedDynamicModel(ModelBakery modelBakery, Function<BlockState, ResourceLocation> baseModelFunction, @Nullable List<Pair<Predicate<BlockState>, BakedModel>> parts, @Nullable Function<BlockState, Map<String, String>> textureMapFunction, @Nullable BiConsumer<BlockState, Matrix4f> transformFunction, ResourceLocation location) {
        this.modelBakery = modelBakery;
        this.baseModelFunction = baseModelFunction;
        this.parts = parts;
        this.textureMapFunction = textureMapFunction;
        this.transformFunction = transformFunction;
        this.location = location;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand) {
        if (state != null) {
            Matrix4f transform = BlockModelRotation.X0_Y0.getRotation().getMatrix();
            String stateString = state.toString();
            BakedModel bakedModel = cache.get(stateString);
            if (bakedModel == null) {
                if(transformFunction != null) {
                    transformFunction.accept(state, transform);
                }

                ModelState modelTransform = new SimpleModelState(new Transformation(transform));

                ResourceLocation baseModelLocation = baseModelFunction.apply(state);

                // If we're going to retexture, we need to ensure the base model has already been baked to prevent circular parent references in the retextured model
                if (textureMapFunction != null && !baseModelCache.containsKey(baseModelLocation)) {
                    final UnbakedModel baseModel = ForgeModelBakery.instance().getModelOrMissing(baseModelLocation);
                    final BakedModel bakedBaseModel = baseModel.bake(modelBakery, ForgeModelBakery.defaultTextureGetter(), modelTransform, baseModelLocation);
                    baseModelCache.put(baseModelLocation, bakedBaseModel);
                }

                UnbakedModel retexturedBaseModel = textureMapFunction != null ? ForgeBalmModels.retexture(modelBakery, baseModelLocation, textureMapFunction.apply(state)) : ForgeModelBakery.instance().getModelOrMissing(baseModelLocation);
                bakedModel = retexturedBaseModel.bake(modelBakery, ForgeModelBakery.defaultTextureGetter(), modelTransform, location);
                cache.put(stateString, bakedModel);

                if (particleTexture == null && bakedModel != null) {
                    particleTexture = bakedModel.getParticleIcon();
                }
            }

            return bakedModel != null ? bakedModel.getQuads(state, side, rand) : Collections.emptyList();
        }

        return Collections.emptyList();
    }

    @Override
    public boolean useAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean isGui3d() {
        return true;
    }

    @Override
    public boolean usesBlockLight() {
        return false;
    }

    @Override
    public boolean isCustomRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return particleTexture != null ? particleTexture : ForgeModelBakery.White.instance();
    }

    @Override
    public ItemOverrides getOverrides() {
        return ItemOverrides.EMPTY;
    }

}
