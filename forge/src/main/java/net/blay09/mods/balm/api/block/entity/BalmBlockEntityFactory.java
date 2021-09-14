package net.blay09.mods.balm.api.block.entity;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public interface BalmBlockEntityFactory<T extends BlockEntity> extends BlockEntityType.BlockEntitySupplier<T> {
}
