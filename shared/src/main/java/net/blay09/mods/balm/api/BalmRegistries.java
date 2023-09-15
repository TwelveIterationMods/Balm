package net.blay09.mods.balm.api;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

import java.util.Collection;

public interface BalmRegistries {
    ResourceLocation getKey(Item item);

    ResourceLocation getKey(Block block);

    ResourceLocation getKey(Fluid fluid);

    ResourceLocation getKey(EntityType<?> entityType);

    ResourceLocation getKey(MenuType<?> menuType);

    Collection<ResourceLocation> getItemKeys();

    Item getItem(ResourceLocation key);

    Block getBlock(ResourceLocation key);

    Fluid getFluid(ResourceLocation key);

    MobEffect getMobEffect(ResourceLocation key);

    TagKey<Item> getItemTag(ResourceLocation key);

    void enableMilkFluid();

    Fluid getMilkFluid();

    Attribute getAttribute(ResourceLocation key);
}
