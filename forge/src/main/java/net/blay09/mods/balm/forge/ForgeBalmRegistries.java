package net.blay09.mods.balm.forge;

import net.blay09.mods.balm.api.BalmRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collection;

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
    public Collection<ResourceLocation> getItemKeys() {
        return ForgeRegistries.ITEMS.getKeys();
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

    @Override
    public void enableMilkFluid() {
        ForgeMod.enableMilkFluid();
    }

    @Override
    public Fluid getMilkFluid() {
        return ForgeMod.MILK.get();
    }
}
