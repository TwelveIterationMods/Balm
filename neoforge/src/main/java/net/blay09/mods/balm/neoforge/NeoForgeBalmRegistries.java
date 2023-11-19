package net.blay09.mods.balm.neoforge;

import net.blay09.mods.balm.api.BalmRegistries;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.NeoForgeMod;

import java.util.Collection;

public class NeoForgeBalmRegistries implements BalmRegistries {
    @Override
    public ResourceLocation getKey(Item item) {
        return BuiltInRegistries.ITEM.getKey(item);
    }

    @Override
    public ResourceLocation getKey(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block);
    }

    @Override
    public ResourceLocation getKey(Fluid fluid) {
        return BuiltInRegistries.FLUID.getKey(fluid);
    }

    @Override
    public ResourceLocation getKey(EntityType<?> entityType) {
        return BuiltInRegistries.ENTITY_TYPE.getKey(entityType);
    }

    @Override
    public ResourceLocation getKey(MenuType<?> menuType) {
        return BuiltInRegistries.MENU.getKey(menuType);
    }

    @Override
    public Collection<ResourceLocation> getItemKeys() {
        return BuiltInRegistries.ITEM.keySet();
    }

    @Override
    public Item getItem(ResourceLocation key) {
        return BuiltInRegistries.ITEM.get(key);
    }

    @Override
    public Block getBlock(ResourceLocation key) {
        return BuiltInRegistries.BLOCK.get(key);
    }

    @Override
    public Fluid getFluid(ResourceLocation key) {
        return BuiltInRegistries.FLUID.get(key);
    }

    @Override
    public MobEffect getMobEffect(ResourceLocation key) {
        return BuiltInRegistries.MOB_EFFECT.get(key);
    }

    @Override
    public TagKey<Item> getItemTag(ResourceLocation key) {
        return ItemTags.create(key);
    }

    @Override
    public void enableMilkFluid() {
        NeoForgeMod.enableMilkFluid();
    }

    @Override
    public Fluid getMilkFluid() {
        return NeoForgeMod.MILK.get();
    }

    @Override
    public Attribute getAttribute(ResourceLocation key) {
        return BuiltInRegistries.ATTRIBUTE.get(key);
    }
}
