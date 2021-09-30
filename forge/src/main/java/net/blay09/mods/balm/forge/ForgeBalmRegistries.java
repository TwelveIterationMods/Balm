package net.blay09.mods.balm.forge;

import net.blay09.mods.balm.api.BalmRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;

public class ForgeBalmRegistries implements BalmRegistries {
    @Override
    public ResourceLocation getKey(Item item) {
        return item.getRegistryName();
    }

    @Override
    public ResourceLocation getKey(Block block) {
        return block.getRegistryName();
    }

    @Override
    public ResourceLocation getKey(Fluid fluid) {
        return fluid.getRegistryName();
    }

    @Override
    public Item getItem(ResourceLocation key) {
        return ForgeRegistries.ITEMS.getValue(key);
    }

    @Override
    public Block getBlock(ResourceLocation key) {
        return ForgeRegistries.BLOCKS.getValue(key);
    }

    @Override
    public Fluid getFluid(ResourceLocation key) {
        return ForgeRegistries.FLUIDS.getValue(key);
    }

    @Override
    public Tag<Item> getItemTag(ResourceLocation key) {
        return ItemTags.bind(key.toString());
    }
}
