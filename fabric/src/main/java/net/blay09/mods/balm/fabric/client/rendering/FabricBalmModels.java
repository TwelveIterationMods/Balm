package net.blay09.mods.balm.fabric.client.rendering;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Matrix4f;
import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.client.rendering.BalmModels;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiConsumer;
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

    private List<DeferredModel> modelsToBake = new ArrayList<>();
    public final List<Pair<Supplier<Block>, Supplier<BakedModel>>> overrides = new ArrayList<>();

    public void onModelBake(ModelBakery bakery) {
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
                return model.bake(bakery, Material::sprite, BlockModelRotation.X0_Y0, identifier);
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
                return model.bake(bakery, Material::sprite, BlockModelRotation.X0_Y0, identifier);
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
// TODO                return new CachedDynamicModel(bakery, effectiveModelFunction, null, textureMapFunction, transformFunction, identifier);
                return null;
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
                return model.bake(bakery, Material::sprite, BlockModelRotation.X0_Y0, identifier);
            }
        };
        modelsToBake.add(deferredModel);
        return deferredModel;
    }

    @Override
    public void overrideModel(Supplier<Block> block, Supplier<BakedModel> model) {
        overrides.add(Pair.of(block, model));
    }

    public static UnbakedModel retexture(ModelBakery bakery, ResourceLocation identifier, Map<String, String> textureMap) {
        Map<String, Either<Material, String>> replacedTexturesMapped = new HashMap<>();
        for (Map.Entry<String, String> entry : textureMap.entrySet()) {
            replacedTexturesMapped.put(entry.getKey(), Either.left(new Material(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation(entry.getValue()))));
        }

        BlockModel blockModel = new BlockModel(identifier, Collections.emptyList(), replacedTexturesMapped, false, BlockModel.GuiLight.FRONT, ItemTransforms.NO_TRANSFORMS, Collections.emptyList());

        // We have to call getMaterials to initialize the parent field in the model (as that is usually done during stitching, which we're already past)
        blockModel.getMaterials(bakery::getModel, new HashSet<>());

        return blockModel;
    }
}
