package net.blay09.mods.balm;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class BalmHooks {
    /**
     * Forge fires SaplingGrowTreeEvent
     */
    public static boolean saplingGrowTree(Level level, Random random, BlockPos pos) {
        return true;
    }

    /**
     * Forge adds player-sensitive version
     */
    public static boolean growCrop(ItemStack itemStack, Level level, BlockPos pos, @Nullable Player player) {
        return BoneMealItem.growCrop(itemStack, level, pos);
    }
}
