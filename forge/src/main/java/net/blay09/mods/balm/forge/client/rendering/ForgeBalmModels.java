package net.blay09.mods.balm.forge.client.rendering;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Matrix4f;
import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.client.rendering.BalmModels;
import net.blay09.mods.balm.forge.client.CachedDynamicModel;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.ForgeModelBakery;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.SimpleModelState;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ForgeBalmModels implements BalmModels {

    private static abstract class DeferredModel extends DeferredObject<BakedModel> {
        public DeferredModel(ResourceLocation identifier) {
            super(identifier);
        }

        public void resolveAndSet(ModelBakery modelBakery, Map<ResourceLocation, BakedModel> modelRegistry) {
            set(resolve(modelBakery, modelRegistry));
        }

        public abstract BakedModel resolve(ModelBakery modelBakery, Map<ResourceLocation, BakedModel> modelRegistry);
    }

    private static class Registrations {
        public final List<DeferredModel> modelsToBake = new ArrayList<>();
        public final List<Pair<Supplier<Block>, Supplier<BakedModel>>> overrides = new ArrayList<>();

        @SubscribeEvent
        public void onBakeModels(ModelBakeEvent event) {
            for (DeferredModel deferredModel : modelsToBake) {
                deferredModel.resolveAndSet(event.getModelLoader(), event.getModelRegistry());
            }

            for (Pair<Supplier<Block>, Supplier<BakedModel>> override : overrides) {
                Block block = override.getFirst().get();
                BakedModel bakedModel = override.getSecond().get();
                block.getStateDefinition().getPossibleStates().forEach((state) -> {
                    ModelResourceLocation modelLocation = BlockModelShaper.stateToModelLocation(state);
                    event.getModelRegistry().put(modelLocation, bakedModel);
                });
            }
        }
    }

    private final Map<String, Registrations> registrations = new ConcurrentHashMap<>();

    @Override
    public DeferredObject<BakedModel> loadModel(ResourceLocation identifier) {
        DeferredModel deferredModel = new DeferredModel(identifier) {
            @Override
            public BakedModel resolve(ModelBakery bakery, Map<ResourceLocation, BakedModel> modelRegistry) {
                UnbakedModel model = bakery.getModel(identifier);
                return model.bake(bakery, ForgeModelBakery.defaultTextureGetter(), SimpleModelState.IDENTITY, identifier);
            }
        };
        getActiveRegistrations().modelsToBake.add(deferredModel);
        return deferredModel;
    }

    @Override
    public DeferredObject<BakedModel> bakeModel(ResourceLocation identifier, UnbakedModel model) {
        DeferredModel deferredModel = new DeferredModel(identifier) {
            @Override
            public BakedModel resolve(ModelBakery bakery, Map<ResourceLocation, BakedModel> modelRegistry) {
                return model.bake(bakery, ForgeModelBakery.defaultTextureGetter(), SimpleModelState.IDENTITY, identifier);
            }
        };
        getActiveRegistrations().modelsToBake.add(deferredModel);
        return deferredModel;
    }

    @Override
    public DeferredObject<BakedModel> retexture(ResourceLocation identifier, Map<String, String> textureMap) {
        DeferredModel deferredModel = new DeferredModel(identifier) {
            @Override
            public BakedModel resolve(ModelBakery bakery, Map<ResourceLocation, BakedModel> modelRegistry) {
                UnbakedModel model = retexture(bakery, identifier, textureMap);
                return model.bake(bakery, ForgeModelBakery.defaultTextureGetter(), SimpleModelState.IDENTITY, identifier);
            }
        };
        getActiveRegistrations().modelsToBake.add(deferredModel);
        return deferredModel;
    }

    @Override
    public DeferredObject<BakedModel> loadDynamicModel(ResourceLocation identifier, @Nullable Function<BlockState, ResourceLocation> modelFunction, @Nullable Function<BlockState, Map<String, String>> textureMapFunction, @Nullable BiConsumer<BlockState, Matrix4f> transformFunction) {
        Function<BlockState, ResourceLocation> effectiveModelFunction = modelFunction != null ? modelFunction : (it -> identifier);

        DeferredModel deferredModel = new DeferredModel(identifier) {
            @Override
            public BakedModel resolve(ModelBakery bakery, Map<ResourceLocation, BakedModel> modelRegistry) {
                return new CachedDynamicModel(bakery, effectiveModelFunction, null, textureMapFunction, transformFunction, identifier);
            }
        };
        getActiveRegistrations().modelsToBake.add(deferredModel);
        return deferredModel;
    }

    @Override
    public void overrideModel(Supplier<Block> block, Supplier<BakedModel> model) {
        getActiveRegistrations().overrides.add(Pair.of(block, model));
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

    public void register() {
        FMLJavaModLoadingContext.get().getModEventBus().register(getActiveRegistrations());
    }

    private Registrations getActiveRegistrations() {
        return registrations.computeIfAbsent(ModLoadingContext.get().getActiveNamespace(), it -> new Registrations());
    }
}
