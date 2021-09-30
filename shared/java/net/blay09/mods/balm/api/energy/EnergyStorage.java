package net.blay09.mods.balm.api.energy;

import net.minecraft.nbt.IntTag;

public class EnergyStorage {

    private final int capacity;
    private final int maxReceive;
    private final int maxExtract;
    private int energy;

    public EnergyStorage(int capacity) {
        this(capacity, capacity, capacity, 0);
    }

    public EnergyStorage(int capacity, int maxTransfer) {
        this(maxTransfer, capacity, maxTransfer, 0);
    }

    public EnergyStorage(int capacity, int maxReceive, int maxExtract) {
        this(maxExtract, capacity, maxReceive, 0);
    }

    public EnergyStorage(int maxExtract, int capacity, int maxReceive, int amount) {
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
        this.energy = Math.max(0, Math.min(capacity, amount));
    }

    public int fill(int maxFill, boolean simulate) {
        if (!canReceive()) {
            return 0;
        }

        int filled = Math.min(capacity - energy, Math.min(maxReceive, maxFill));
        if (!simulate) {
            energy += filled;
        }
        return filled;
    }

    public int drain(int maxDrain, boolean simulate) {
        if (!canExtract()) {
            return 0;
        }

        int drained = Math.min(energy, Math.min(this.maxExtract, maxDrain));
        if (!simulate) {
            energy -= drained;
        }
        return drained;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean canExtract() {
        return maxExtract > 0;
    }

    public boolean canReceive() {
        return maxReceive > 0;
    }

    public IntTag serialize() {
        return IntTag.valueOf(energy);
    }

    public void deserialize(IntTag tag) {
        energy = tag.getAsInt();
    }
}
