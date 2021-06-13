package net.blay09.mods.forbic.block.entity;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface ForbicBlockEntityFactory<T extends BlockEntity> extends FabricBlockEntityTypeBuilder.Factory<T> {
}
