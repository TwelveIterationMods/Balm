package net.blay09.mods.forbic;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ForbicBlockEntities {
    public static <T extends BlockEntity> BlockEntityType<T> register(ResourceLocation identifier, ForbicBlockFactory<T> factory, Block... blocks) {
        BlockEntityType<T> type = FabricBlockEntityTypeBuilder.create(factory, blocks).build();
        return Registry.register(Registry.BLOCK_ENTITY_TYPE, identifier.toString(), type);
    }
}
