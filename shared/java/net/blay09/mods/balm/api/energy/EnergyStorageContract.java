package net.blay09.mods.balm.api.energy;

public interface EnergyStorageContract {
    int receiveEnergy(int maxReceive, boolean simulate);
    int extractEnergy(int maxExtract, boolean simulate);
    void setEnergyStored(int energy);
    int getEnergyStored();
    int getMaxEnergyStored();
    boolean canExtract();
    boolean canReceive();
}
