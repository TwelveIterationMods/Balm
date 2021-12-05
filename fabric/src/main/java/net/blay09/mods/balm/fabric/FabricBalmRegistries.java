package net.blay09.mods.balm.fabric;

import net.blay09.mods.balm.api.BalmRegistries;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import java.util.Collection;

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
    public ResourceLocation getKey(EntityType<?> entityType) {
        return Registry.ENTITY_TYPE.getKey(entityType);
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

    @Override
    public MobEffect getMobEffect(ResourceLocation key) {
        return Registry.MOB_EFFECT.get(key);
    }

    @Override
    public Tag<Item> getItemTag(ResourceLocation key) {
        return TagRegistry.item(key);
    }

    @Override
    public void enableMilkFluid() {
        // TODO Fluids
    }

    @Override
    public Fluid getMilkFluid() {
        return Fluids.LAVA; // TODO Fluids
    }

    @Override
    public Collection<ResourceLocation> getItemKeys() {
        return Registry.ITEM.keySet();
    }
}
