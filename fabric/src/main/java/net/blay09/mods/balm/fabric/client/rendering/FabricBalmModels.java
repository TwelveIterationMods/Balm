package net.blay09.mods.balm.fabric.client.rendering;

import com.mojang.datafixers.util.Pair;
import com.mojang.math.Transformation;
import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.client.rendering.BalmModels;
import net.blay09.mods.balm.common.CachedDynamicModel;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class FabricBalmModels implements BalmModels {

    private static abstract class DeferredModel extends DeferredObject<BakedModel> {
        public DeferredModel(ResourceLocation identifier) {
            super(identifier);
        }

        public void resolveAndSet(ModelBakery modelBakery) {
            set(resolve(modelBakery));
        }

        public abstract BakedModel resolve(ModelBakery modelBakery);
    }

    private static ModelBakery bakery;

    private final List<DeferredModel> modelsToBake = new ArrayList<>();
    public final List<Pair<Supplier<Block>, Supplier<BakedModel>>> overrides = new ArrayList<>();
    private ModelBakery modelBakery;
    private BiFunction<ResourceLocation, Material, TextureAtlasSprite> spriteBiFunction;

    public void onBakeModels(ModelBakery modelBakery, BiFunction<ResourceLocation, Material, TextureAtlasSprite> spriteBiFunction) {
        this.modelBakery = modelBakery;
        this.spriteBiFunction = spriteBiFunction;

        for (DeferredModel model : modelsToBake) {
            model.resolveAndSet(bakery);
        }

        for (Pair<Supplier<Block>, Supplier<BakedModel>> override : overrides) {
            Block block = override.getFirst().get();
            BakedModel bakedModel = override.getSecond().get();
            block.getStateDefinition().getPossibleStates().forEach((state) -> {
                ModelResourceLocation modelLocation = BlockModelShaper.stateToModelLocation(state);
                bakery.getBakedTopLevelModels().put(modelLocation, bakedModel);
            });
        }
    }

    @Override
    public DeferredObject<BakedModel> loadModel(final ResourceLocation identifier) {
        DeferredModel deferredModel = new DeferredModel(identifier) {
            @Override
            public BakedModel resolve(ModelBakery bakery) {
                UnbakedModel model = bakery.getModel(identifier);
                return model.bake(createBaker(identifier), Material::sprite, getModelState(Transformation.identity()), identifier);
            }
        };
        modelsToBake.add(deferredModel);
        return deferredModel;
    }

    @Override
    public DeferredObject<BakedModel> bakeModel(ResourceLocation identifier, UnbakedModel model) {
        DeferredModel deferredModel = new DeferredModel(identifier) {
            @Override
            public BakedModel resolve(ModelBakery bakery) {
                return model.bake(createBaker(identifier), Material::sprite, getModelState(Transformation.identity()), identifier);
            }
        };
        modelsToBake.add(deferredModel);
        return deferredModel;
    }

    @Override
    public DeferredObject<BakedModel> loadDynamicModel(ResourceLocation identifier, @Nullable Function<BlockState, ResourceLocation> modelFunction, @Nullable Function<BlockState, Map<String, String>> textureMapFunction, @Nullable BiConsumer<BlockState, Matrix4f> transformFunction) {
        Function<BlockState, ResourceLocation> effectiveModelFunction = modelFunction != null ? modelFunction : (it -> identifier);
        DeferredModel deferredModel = new DeferredModel(identifier) {
            @Override
            public BakedModel resolve(ModelBakery bakery) {
                return new CachedDynamicModel(bakery, effectiveModelFunction, null, textureMapFunction, transformFunction, identifier);
            }
        };
        modelsToBake.add(deferredModel);
        return deferredModel;
    }

    @Override
    public DeferredObject<BakedModel> retexture(ResourceLocation identifier, Map<String, String> textureMap) {
        DeferredModel deferredModel = new DeferredModel(identifier) {
            @Override
            public BakedModel resolve(ModelBakery bakery) {
                UnbakedModel model = retexture(bakery, identifier, textureMap);
                return model.bake(createBaker(identifier), Material::sprite, getModelState(Transformation.identity()), identifier);
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
        return bakery.getModel(location);
    }

    @Override
    public UnbakedModel getUnbakedMissingModel() {
        return bakery.getModel(ModelBakery.MISSING_MODEL_LOCATION);
    }

    @Override
    public ModelBaker createBaker(ResourceLocation location) {
        try {
            Class<?> clazz = Class.forName("net.minecraft.client.resources.model.ModelBakery$ModelBakerImpl");
            Constructor<?> constructor = clazz.getDeclaredConstructor(ModelBakery.class, BiFunction.class, ResourceLocation.class);
            constructor.setAccessible(true);
            return (ModelBaker) constructor.newInstance(bakery, spriteBiFunction, location);
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Balm failed to create model baker", e);
        }
    }
}
