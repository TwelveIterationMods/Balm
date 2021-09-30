package net.blay09.mods.balm.forge;

import net.blay09.mods.balm.api.BalmHooks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.Random;

public class ForgeBalmHooks implements BalmHooks {
    @Override
    public boolean saplingGrowTree(Level level, Random random, BlockPos pos) {
        return ForgeEventFactory.saplingGrowTree(level, random, pos);
    }

    @Override
    public boolean growCrop(ItemStack itemStack, Level level, BlockPos pos, Player player) {
        if (player != null) {
            return BoneMealItem.applyBonemeal(itemStack, level, pos, player);
        } else {
            return BoneMealItem.growCrop(itemStack, level, pos);
        }
    }

    @Override
    public CompoundTag getPersistentData(Player player) {
        CompoundTag persistentData = player.getPersistentData();
        CompoundTag persistedTag = persistentData.getCompound(Player.PERSISTED_NBT_TAG);
        CompoundTag balmData = persistedTag.getCompound("BalmData");
        persistedTag.put("BalmData", balmData);
        persistentData.put(Player.PERSISTED_NBT_TAG, persistedTag);
        return balmData;
    }

    @Override
    public void curePotionEffects(LivingEntity entity, ItemStack curativeItem) {
        entity.curePotionEffects(curativeItem);
    }

    @Override
    public boolean isFakePlayer(Player player) {
        return player instanceof FakePlayer;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        return itemStack.getContainerItem();
    }

    @Override
    public DyeColor getColor(ItemStack itemStack) {
        return DyeColor.getColor(itemStack);
    }

    @Override
    public boolean canItemsStack(ItemStack first, ItemStack second) {
        return ItemHandlerHelper.canItemStacksStack(first, second);
    }

    @Override
    public int getBurnTime(ItemStack itemStack) {
        return ForgeHooks.getBurnTime(itemStack, RecipeType.SMELTING);
    }
}
