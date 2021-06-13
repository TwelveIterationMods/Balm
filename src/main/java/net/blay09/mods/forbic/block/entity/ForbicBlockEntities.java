package net.blay09.mods.forbic.block.entity;

import net.blay09.mods.forbic.core.DeferredObject;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Arrays;
import java.util.stream.Stream;

public class ForbicBlockEntities {
    protected static <T extends BlockEntity> DeferredObject<BlockEntityType<T>> registerBlockEntity(ResourceLocation identifier, ForbicBlockEntityFactory<T> factory, Block... blocks) {
        return new DeferredObject<>(() -> {
            BlockEntityType<T> type = FabricBlockEntityTypeBuilder.create(factory, blocks).build();
            return Registry.register(Registry.BLOCK_ENTITY_TYPE, identifier.toString(), type);
        }).resolveImmediately();
    }

    protected static <T extends BlockEntity> DeferredObject<BlockEntityType<T>> registerBlockEntity(ResourceLocation identifier, ForbicBlockEntityFactory<T> factory, DeferredObject<Block>... blocks) {
        return new DeferredObject<>(() -> {
            Block[] resolvedBlocks = Arrays.stream(blocks).map(DeferredObject::get).toArray(Block[]::new);
            BlockEntityType<T> type = FabricBlockEntityTypeBuilder.create(factory, resolvedBlocks).build();
            return Registry.register(Registry.BLOCK_ENTITY_TYPE, identifier.toString(), type);
        }).resolveImmediately();
    }
}
