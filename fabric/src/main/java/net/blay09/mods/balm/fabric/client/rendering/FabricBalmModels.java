package net.blay09.mods.balm.fabric.client.rendering;

import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import com.mojang.math.Transformation;
import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.client.rendering.BalmModels;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.slf4j.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class FabricBalmModels implements BalmModels {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static abstract class DeferredModel extends DeferredObject<BakedModel> {
        public DeferredModel(ResourceLocation identifier) {
            super(identifier);
        }

        public void resolveAndSet(ModelBakery modelBakery, BiFunction<ResourceLocation, Material, TextureAtlasSprite> spriteBiFunction) {
            try {
                set(resolve(modelBakery, spriteBiFunction));
            } catch (Exception exception) {
                LOGGER.warn("Unable to bake model: '{}':", getIdentifier(), exception);
                set(modelBakery.getBakedTopLevelModels().get(ModelBakery.MISSING_MODEL_LOCATION));
            }
        }

        public abstract BakedModel resolve(ModelBakery modelBakery, BiFunction<ResourceLocation, Material, TextureAtlasSprite> spriteBiFunction);
    }


    private final List<DeferredModel> modelsToBake = Collections.synchronizedList(new ArrayList<>());
    public final List<Pair<Supplier<Block>, Supplier<BakedModel>>> overrides = Collections.synchronizedList(new ArrayList<>());
    private ModelBakery modelBakery;

    public void onBakeModels(ModelBakery modelBakery, BiFunction<ResourceLocation, Material, TextureAtlasSprite> spriteBiFunction) {
        this.modelBakery = modelBakery;

        synchronized (modelsToBake) {
            for (DeferredModel model : modelsToBake) {
                model.resolveAndSet(modelBakery, spriteBiFunction);
            }
        }

        synchronized (overrides) {
            for (Pair<Supplier<Block>, Supplier<BakedModel>> override : overrides) {
                Block block = override.getFirst().get();
                BakedModel bakedModel = override.getSecond().get();
                block.getStateDefinition().getPossibleStates().forEach((state) -> {
                    ModelResourceLocation modelLocation = BlockModelShaper.stateToModelLocation(state);
                    modelBakery.getBakedTopLevelModels().put(modelLocation, bakedModel);
                });
            }
        }
    }

    @Override
    public DeferredObject<BakedModel> loadModel(final ResourceLocation identifier) {
        DeferredModel deferredModel = new DeferredModel(identifier) {
            @Override
            public BakedModel resolve(ModelBakery bakery, BiFunction<ResourceLocation, Material, TextureAtlasSprite> spriteBiFunction) {
                UnbakedModel model = bakery.getModel(identifier);
                ModelBaker baker = createBaker(identifier, spriteBiFunction);
                Function<Material, TextureAtlasSprite> modelTextureGetter = createModelTextureGetter(identifier, spriteBiFunction);
                return model.bake(baker, modelTextureGetter, getModelState(Transformation.identity()), identifier);
            }
        };
        modelsToBake.add(deferredModel);
        return deferredModel;
    }

    @Override
    public DeferredObject<BakedModel> bakeModel(ResourceLocation identifier, UnbakedModel model) {
        DeferredModel deferredModel = new DeferredModel(identifier) {
            @Override
            public BakedModel resolve(ModelBakery bakery, BiFunction<ResourceLocation, Material, TextureAtlasSprite> spriteBiFunction) {
                ModelBaker baker = createBaker(identifier, spriteBiFunction);
                Function<Material, TextureAtlasSprite> modelTextureGetter = createModelTextureGetter(identifier, spriteBiFunction);
                return model.bake(baker, modelTextureGetter, getModelState(Transformation.identity()), identifier);
            }
        };
        modelsToBake.add(deferredModel);
        return deferredModel;
    }

    @Override
    public DeferredObject<BakedModel> loadDynamicModel(ResourceLocation identifier, @Nullable Function<BlockState, ResourceLocation> modelFunction, @Nullable Function<BlockState, Map<String, String>> textureMapFunction, @Nullable BiConsumer<BlockState, Matrix4f> transformFunction, List<RenderType> renderTypes) {
        Function<BlockState, ResourceLocation> effectiveModelFunction = modelFunction != null ? modelFunction : (it -> identifier);
        DeferredModel deferredModel = new DeferredModel(identifier) {
            @Override
            public BakedModel resolve(ModelBakery bakery, BiFunction<ResourceLocation, Material, TextureAtlasSprite> spriteBiFunction) {
                return new FabricCachedDynamicModel(bakery, effectiveModelFunction, null, textureMapFunction, transformFunction, renderTypes, identifier);
            }
        };
        modelsToBake.add(deferredModel);
        return deferredModel;
    }

    @Override
    public DeferredObject<BakedModel> retexture(ResourceLocation identifier, Map<String, String> textureMap) {
        DeferredModel deferredModel = new DeferredModel(identifier) {
            @Override
            public BakedModel resolve(ModelBakery bakery, BiFunction<ResourceLocation, Material, TextureAtlasSprite> spriteBiFunction) {
                UnbakedModel model = retexture(bakery, identifier, textureMap);
                ModelBaker baker = createBaker(identifier, spriteBiFunction);
                Function<Material, TextureAtlasSprite> modelTextureGetter = createModelTextureGetter(identifier, spriteBiFunction);
                return model.bake(baker, modelTextureGetter, getModelState(Transformation.identity()), identifier);
            }
        };
        modelsToBake.add(deferredModel);
        return deferredModel;
    }

    @Override
    public void overrideModel(Supplier<Block> block, Supplier<BakedModel> model) {
        overrides.add(Pair.of(block, model));
    }

    @Override
    public ModelState getModelState(Transformation transformation) {
        return new TransformationModelState(transformation);
    }

    @Override
    public UnbakedModel getUnbakedModelOrMissing(ResourceLocation location) {
        return modelBakery.getModel(location);
    }

    @Override
    public UnbakedModel getUnbakedMissingModel() {
        return modelBakery.getModel(ModelBakery.MISSING_MODEL_LOCATION);
    }

    @Override
    public ModelBaker createBaker(ResourceLocation location, BiFunction<ResourceLocation, Material, TextureAtlasSprite> spriteBiFunction) {
        try {
            MappingResolver mappings = FabricLoader.getInstance().getMappingResolver();
            Class<?> clazz = Class.forName(mappings.mapClassName("intermediary", "net.minecraft.class_1088$class_7778"));
            Constructor<?> constructor = clazz.getDeclaredConstructor(ModelBakery.class, BiFunction.class, ResourceLocation.class);
            constructor.setAccessible(true);
            return (ModelBaker) constructor.newInstance(modelBakery, spriteBiFunction, location);
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Balm failed to create model baker", e);
        }
    }

    private Function<Material, TextureAtlasSprite> createModelTextureGetter(ResourceLocation location, BiFunction<ResourceLocation, Material, TextureAtlasSprite> spriteBiFunction) {
        return (Material material) -> (TextureAtlasSprite) spriteBiFunction.apply(location, material);
    }
}
