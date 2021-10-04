package net.blay09.mods.balm.api.fluid;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.blay09.mods.balm.api.provider.BalmProviderHolder;
import net.minecraft.core.Direction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public interface BalmFluidTankProvider extends BalmProviderHolder {
    FluidTank getFluidTank();

    default FluidTank getFluidTank(Direction side) {
        return getFluidTank();
    }

    @Override
    default List<Object> getProviders() {
        FluidTank fluidTank = getFluidTank();
        if (fluidTank != null) {
            return Lists.newArrayList(fluidTank);
        }

        return Collections.emptyList();
    }

    @Override
    default List<Pair<Direction, Object>> getSidedProviders() {
        List<Pair<Direction, Object>> providers = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            FluidTank fluidTank = getFluidTank(direction);
            if (fluidTank != null) {
                providers.add(Pair.of(direction, fluidTank));
            }
        }
        return providers;
    }
}
