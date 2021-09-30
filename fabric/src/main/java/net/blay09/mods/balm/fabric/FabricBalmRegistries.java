package net.blay09.mods.balm.fabric;

import net.blay09.mods.balm.api.BalmRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public class FabricBalmRegistries implements BalmRegistries {
    @Override
    public ResourceLocation getKey(Item item) {
        return Registry.ITEM.getKey(item);
    }

    @Override
    public ResourceLocation getKey(Block block) {
        return Registry.BLOCK.getKey(block);
    }

    @Override
    public ResourceLocation getKey(Fluid fluid) {
        return Registry.FLUID.getKey(fluid);
    }

    @Override
    public Item getItem(ResourceLocation key) {
        return Registry.ITEM.get(key);
    }

    @Override
    public Block getBlock(ResourceLocation key) {
        return Registry.BLOCK.get(key);
    }

    @Override
    public Fluid getFluid(ResourceLocation key) {
        return Registry.FLUID.get(key);
    }
}
