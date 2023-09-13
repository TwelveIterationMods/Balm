package net.blay09.mods.balm.fabric;

import net.blay09.mods.balm.api.BalmRegistries;
import net.blay09.mods.balm.fabric.fluid.SimpleMilkFluid;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

import java.util.Collection;

public class FabricBalmRegistries implements BalmRegistries {
    public Fluid milkFluid;

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
    public TagKey<Item> getItemTag(ResourceLocation key) {
        return TagKey.create(Registry.ITEM_REGISTRY, key);
    }

    @Override
    public void enableMilkFluid() {
        milkFluid = Registry.register(Registry.FLUID, new ResourceLocation("balm-fabric", "milk"), new SimpleMilkFluid());
    }

    @Override
    public Fluid getMilkFluid() {
        return milkFluid;
    }

    @Override
    public Collection<ResourceLocation> getItemKeys() {
        return Registry.ITEM.keySet();
    }

    @Override
    public Attribute getAttribute(ResourceLocation key) {
        return BuiltInRegistries.ATTRIBUTE.get(key);
    }
}
