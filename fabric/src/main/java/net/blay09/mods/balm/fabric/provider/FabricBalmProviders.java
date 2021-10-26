package net.blay09.mods.balm.fabric.provider;

import net.blay09.mods.balm.api.provider.BalmProviders;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;

public class FabricBalmProviders implements BalmProviders {
    @Override
    public <T> T getProvider(BlockEntity blockEntity, Class<T> clazz) {
        return null; // TODO
    }

    @Override
    public <T> T getProvider(BlockEntity blockEntity, Direction direction, Class<T> clazz) {
        return null; // TODO
    }

    @Override
    public <T> T getProvider(Entity entity, Class<T> clazz) {
        return null; // TODO
    }
}
