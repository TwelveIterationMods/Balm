package net.blay09.mods.balm.api;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public interface BalmHooks {
    /**
     * Forge fires SaplingGrowTreeEvent, Fabric NOPs
     */
    boolean saplingGrowTree(Level level, Random random, BlockPos pos);

    /**
     * Forge adds player-sensitive version, Fabric uses normal version
     */
    boolean growCrop(ItemStack itemStack, Level level, BlockPos pos, @Nullable Player player);

    /**
     * Forge provides a tag in player data that is persisted across clones and death.
     * Fabric does not provide such a tag; so we add our own
     */
    CompoundTag getPersistentData(Player player);

    /**
     * Forge provides a patch to cure potion effects specific to a given curative item.
     * Fabric does not provide such patch, so on Fabric this will always clear all effects.
     */
    void curePotionEffects(LivingEntity entity, ItemStack curativeItem);

    boolean isFakePlayer(Player player);

    ItemStack getCraftingRemainingItem(ItemStack itemStack);
}
