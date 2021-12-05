package net.blay09.mods.balm.fabric;

import net.blay09.mods.balm.api.BalmHooks;
import net.blay09.mods.balm.api.entity.BalmEntity;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class FabricBalmHooks implements BalmHooks {
    public boolean saplingGrowTree(Level level, Random random, BlockPos pos) {
        return true;
    }

    public boolean growCrop(ItemStack itemStack, Level level, BlockPos pos, @Nullable Player player) {
        return BoneMealItem.growCrop(itemStack, level, pos);
    }

    @Override
    public CompoundTag getPersistentData(Entity entity) {
        return ((BalmEntity) entity).getBalmData();
    }

    @Override
    public void curePotionEffects(LivingEntity entity, ItemStack curativeItem) {
        entity.removeAllEffects();
    }

    @Override
    public boolean isFakePlayer(Player player) {
        return false;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        return new ItemStack(itemStack.getItem().getCraftingRemainingItem());
    }

    @Override
    public DyeColor getColor(ItemStack itemStack) {
        if (itemStack.getItem() instanceof DyeItem) {
            return ((DyeItem) itemStack.getItem()).getDyeColor();
        }

        return null;
    }

    @Override
    public boolean canItemsStack(ItemStack first, ItemStack second) {
        return !first.isEmpty() && first.sameItem(second) && first.hasTag() == second.hasTag() && (!first.hasTag() || first.getTag().equals(second.getTag()));
    }

    @Override
    public int getBurnTime(ItemStack itemStack) {
        return FuelRegistry.INSTANCE.get(itemStack.getItem());
    }

    @Override
    public void firePlayerCraftingEvent(Player player, ItemStack crafted, Container craftMatrix) {
    }

    @Override
    public boolean useFluidTank(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        return false; // TODO Fluids
    }

    @Override
    public boolean isShield(ItemStack itemStack) {
        return itemStack.getItem() instanceof ShieldItem;
    }
}
