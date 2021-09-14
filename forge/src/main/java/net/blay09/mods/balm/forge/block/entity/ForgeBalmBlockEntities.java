package net.blay09.mods.balm.forge.block.entity;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.block.BalmBlockEntities;
import net.blay09.mods.balm.api.block.entity.BalmBlockEntityFactory;
import net.blay09.mods.balm.forge.DeferredRegisters;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;

public class ForgeBalmBlockEntities implements BalmBlockEntities {
    @Override
    public <T extends BlockEntity> DeferredObject<BlockEntityType<T>> registerBlockEntity(ResourceLocation identifier, BalmBlockEntityFactory<T> factory, Block... blocks) {
        DeferredRegister<BlockEntityType<?>> register = DeferredRegisters.get(ForgeRegistries.BLOCK_ENTITIES, identifier.getNamespace());
        RegistryObject<BlockEntityType<T>> registryObject = register.register(identifier.getPath(), () -> BlockEntityType.Builder.of(factory, blocks).build(null));
        return new DeferredObject<>(identifier, registryObject);
    }

    @Override
    public <T extends BlockEntity> DeferredObject<BlockEntityType<T>> registerBlockEntity(ResourceLocation identifier, BalmBlockEntityFactory<T> factory, DeferredObject<Block>... blocks) {
        DeferredRegister<BlockEntityType<?>> register = DeferredRegisters.get(ForgeRegistries.BLOCK_ENTITIES, identifier.getNamespace());
        RegistryObject<BlockEntityType<T>> registryObject = register.register(identifier.getPath(), () -> {
            Block[] resolvedBlocks = Arrays.stream(blocks).map(DeferredObject::get).toArray(Block[]::new);
            return BlockEntityType.Builder.of(factory, resolvedBlocks).build(null);
        });
        return new DeferredObject<>(identifier, registryObject);
    }
}
