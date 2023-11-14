package net.blay09.mods.balm.neoforge.fluid;

import net.blay09.mods.balm.api.fluid.FluidTank;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

public class NeoForgeFluidTank implements IFluidHandler {

    private final FluidTank fluidTank;

    public NeoForgeFluidTank(FluidTank fluidTank) {
        this.fluidTank = fluidTank;
    }

    @Override
    public int getTanks() {
        return 1;
    }

    @NotNull
    @Override
    public FluidStack getFluidInTank(int tank) {
        return new FluidStack(fluidTank.getFluid(), fluidTank.getAmount());
    }

    @Override
    public int getTankCapacity(int tank) {
        return fluidTank.getCapacity();
    }

    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        return fluidTank.canFill(stack.getFluid());
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        return fluidTank.fill(resource.getFluid(), resource.getAmount(), action.simulate());
    }

    @NotNull
    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        int drained = fluidTank.drain(resource.getFluid(), resource.getAmount(), action.simulate());
        return new FluidStack(fluidTank.getFluid(), drained);
    }

    @NotNull
    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        int drained = fluidTank.drain(fluidTank.getFluid(), maxDrain, action.simulate());
        return new FluidStack(fluidTank.getFluid(), drained);
    }
}
