package net.blay09.mods.forbic.block.entity;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ForbicBlockEntities {
    protected static <T extends BlockEntity> BlockEntityType<T> register(ResourceLocation identifier, ForbicBlockEntityFactory<T> factory, Block... blocks) {
        BlockEntityType<T> type = FabricBlockEntityTypeBuilder.create(factory, blocks).build();
        return Registry.register(Registry.BLOCK_ENTITY_TYPE, identifier.toString(), type);
    }
}
