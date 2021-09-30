package net.blay09.mods.balm.api.energy;

public class EnergyStorage extends net.minecraftforge.energy.EnergyStorage implements EnergyStorageContract {

    public EnergyStorage(int capacity) {
        super(capacity);
    }

    @Override
    public void setEnergyStored(int energy) {
        this.energy = energy;
    }

}
