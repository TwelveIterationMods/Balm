package net.blay09.mods.balm.api.container;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.blay09.mods.balm.api.provider.BalmProviderHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public interface BalmContainerProvider {
    Container getContainer();

    default Container getContainer(Direction side) {
        return getContainer();
    }

    default void dropItems(Level level, BlockPos pos) {
        Container container = getContainer();
        ContainerUtils.dropItems(container, level, pos);
    }

    default ItemStack extractItem(int slot, int amount, boolean simulate) {
        Container container = getContainer();
        return ContainerUtils.extractItem(container, slot, amount, simulate);
    }

    default ItemStack insertItem(ItemStack itemStack, int slot, boolean simulate) {
        Container container = getContainer();
        return ContainerUtils.insertItem(container, slot, itemStack, simulate);
    }

    default ItemStack insertItemStacked(ItemStack itemStack, boolean simulate) {
        Container container = getContainer();
        return ContainerUtils.insertItemStacked(container, itemStack, simulate);
    }
}
