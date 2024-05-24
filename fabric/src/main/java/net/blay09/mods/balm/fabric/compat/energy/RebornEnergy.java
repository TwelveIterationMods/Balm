package net.blay09.mods.balm.fabric.compat.energy;

import net.blay09.mods.balm.api.energy.BalmEnergyStorageProvider;
import team.reborn.energy.api.EnergyStorage;

public class RebornEnergy {
    public RebornEnergy() {
        EnergyStorage.SIDED.registerFallback((world, pos, state, blockEntity, direction) -> {
            if (blockEntity instanceof BalmEnergyStorageProvider energyStorageProvider) {
                final var energyStorage = energyStorageProvider.getEnergyStorage(direction);
                if (energyStorage != null) {
                    return new RebornEnergyStorage(energyStorage);
                }
            }

            return null;
        });
    }
}
