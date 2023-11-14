package net.blay09.mods.balm.neoforge.energy;

import net.blay09.mods.balm.api.energy.EnergyStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;

public class NeoForgeEnergyStorage implements IEnergyStorage {

    private final EnergyStorage energyStorage;

    public NeoForgeEnergyStorage(EnergyStorage energyStorage) {
        this.energyStorage = energyStorage;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return energyStorage.fill(maxReceive, simulate);
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return energyStorage.drain(maxExtract, simulate);
    }

    @Override
    public int getEnergyStored() {
        return energyStorage.getEnergy();
    }

    @Override
    public int getMaxEnergyStored() {
        return energyStorage.getCapacity();
    }

    @Override
    public boolean canExtract() {
        return energyStorage.canDrain();
    }

    @Override
    public boolean canReceive() {
        return energyStorage.canFill();
    }
}
