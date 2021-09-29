package net.blay09.mods.balm.api.container;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ContainerUtils {
    public static ItemStack extractItem(Container container, int slot, int amount, boolean simulate) {
        return ItemStack.EMPTY;
    }

    public static ItemStack insertItem(Container container, ItemStack itemStack, boolean simulate) {
        return itemStack;
    }

    public static ItemStack insertItemStacked(Container container, ItemStack itemStack, boolean simulate) {
        return itemStack;
    }

    public static void dropItems(Container container, Level level, BlockPos pos) {
        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack itemStack = container.getItem(i);
            if (!itemStack.isEmpty()) {
                ItemEntity itemEntity = new ItemEntity(level, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, itemStack);
                itemEntity.setDeltaMovement(0f, 0.2f, 0f);
                level.addFreshEntity(itemEntity);
            }
        }
        container.clearContent();
    }
}