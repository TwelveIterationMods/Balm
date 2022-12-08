package net.blay09.mods.balm.api.container;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Set;

public class DelegateContainer implements Container, ExtractionAwareContainer {
    private final Container delegate;

    public DelegateContainer(Container delegate) {
        this.delegate = delegate;
    }

    @Override
    public int getMaxStackSize() {
        return delegate.getMaxStackSize();
    }

    @Override
    public void startOpen(Player player) {
        delegate.startOpen(player);
    }

    @Override
    public void stopOpen(Player player) {
        delegate.stopOpen(player);
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack itemStack) {
        return delegate.canPlaceItem(slot, itemStack);
    }

    @Override
    public int countItem(Item item) {
        return delegate.countItem(item);
    }

    @Override
    public boolean hasAnyOf(Set<Item> items) {
        return delegate.hasAnyOf(items);
    }

    @Override
    public int getContainerSize() {
        return delegate.getContainerSize();
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    @Override
    public ItemStack getItem(int slot) {
        return delegate.getItem(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int count) {
        return delegate.removeItem(slot, count);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return delegate.removeItemNoUpdate(slot);
    }

    @Override
    public void setItem(int slot, ItemStack itemStack) {
        delegate.setItem(slot, itemStack);
    }

    @Override
    public void setChanged() {
        delegate.setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        return delegate.stillValid(player);
    }

    @Override
    public void clearContent() {
        delegate.clearContent();
    }

    @Override
    public boolean canExtractItem(int slot) {
        if (delegate instanceof ExtractionAwareContainer extractionAwareContainer) {
            return extractionAwareContainer.canExtractItem(slot);
        }
        return true;
    }
}
