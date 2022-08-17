package net.blay09.mods.balm.api.container;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;

public class CombinedContainer implements Container, ExtractionAwareContainer {
    private final Container[] containers;
    private final int[] baseIndex;
    private final int totalSlots;

    public CombinedContainer(Container... containers) {
        this.containers = containers;
        this.baseIndex = new int[containers.length];
        int index = 0;
        for (int i = 0; i < containers.length; i++) {
            index += containers[i].getContainerSize();
            baseIndex[i] = index;
        }
        this.totalSlots = index;
    }

    private int getContainerIndexForSlot(int slot) {
        if (slot < 0) {
            return -1;
        }

        for (int i = 0; i < baseIndex.length; i++) {
            if (slot - baseIndex[i] < 0) {
                return i;
            }
        }

        return -1;
    }

    private Container getContainerFromIndex(int index) {
        return index >= 0 && index < containers.length ? containers[index] : EmptyContainer.INSTANCE;
    }

    private int getSlotFromIndex(int slot, int index) {
        return index > 0 && index < baseIndex.length ? slot - baseIndex[index - 1] : slot;

    }

    @Override
    public int getContainerSize() {
        return totalSlots;
    }

    @Override
    public boolean isEmpty() {
        return Arrays.stream(containers).allMatch(Container::isEmpty);
    }

    @Override
    public ItemStack getItem(int slot) {
        int containerIndex = getContainerIndexForSlot(slot);
        Container container = getContainerFromIndex(containerIndex);
        return container.getItem(getSlotFromIndex(slot, containerIndex));
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        int containerIndex = getContainerIndexForSlot(slot);
        Container container = getContainerFromIndex(containerIndex);
        return container.removeItem(getSlotFromIndex(slot, containerIndex), amount);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        int containerIndex = getContainerIndexForSlot(slot);
        Container container = getContainerFromIndex(containerIndex);
        return container.removeItemNoUpdate(getSlotFromIndex(slot, containerIndex));
    }

    @Override
    public void setItem(int slot, ItemStack itemStack) {
        int containerIndex = getContainerIndexForSlot(slot);
        Container container = getContainerFromIndex(containerIndex);
        container.setItem(getSlotFromIndex(slot, containerIndex), itemStack);
    }

    @Override
    public void setChanged() {
        for (Container container : containers) {
            container.setChanged();
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return Arrays.stream(containers).allMatch(container -> container.stillValid(player));
    }

    @Override
    public void clearContent() {
        for (Container container : containers) {
            container.clearContent();
        }
    }

    @Override
    public boolean canExtractItem(int slot) {
        int containerIndex = getContainerIndexForSlot(slot);
        Container container = getContainerFromIndex(containerIndex);
        if (container instanceof ExtractionAwareContainer extractionAwareContainer) {
            return extractionAwareContainer.canExtractItem(getSlotFromIndex(slot, containerIndex));
        }
        return true;
    }
}
