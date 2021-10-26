package net.blay09.mods.balm.api;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

import java.util.Collection;

public interface BalmRegistries {
    ResourceLocation getKey(Item item);

    ResourceLocation getKey(Block block);

    ResourceLocation getKey(Fluid fluid);

    Collection<ResourceLocation> getItemKeys();

    Item getItem(ResourceLocation key);

    Block getBlock(ResourceLocation key);

    Fluid getFluid(ResourceLocation key);

    MobEffect getMobEffect(ResourceLocation key);

    Tag<Item> getItemTag(ResourceLocation key);

    void enableMilkFluid();

    Fluid getMilkFluid();
}
