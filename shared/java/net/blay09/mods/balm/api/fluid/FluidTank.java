package net.blay09.mods.balm.api.fluid;

import net.blay09.mods.balm.api.Balm;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

public class FluidTank {
    private final int capacity;
    private final int maxReceive;
    private final int maxExtract;
    private Fluid fluid = Fluids.EMPTY;
    private int amount;

    public FluidTank(int capacity) {
        this(capacity, capacity, capacity, 0);
    }

    public FluidTank(int capacity, int maxTransfer) {
        this(maxTransfer, capacity, maxTransfer, 0);
    }

    public FluidTank(int capacity, int maxReceive, int maxExtract) {
        this(maxExtract, capacity, maxReceive, 0);
    }

    public FluidTank(int maxExtract, int capacity, int maxReceive, int amount) {
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
        this.amount = Math.max(0, Math.min(capacity, amount));
    }

    public int fill(Fluid fluid, int maxFill, boolean simulate) {
        if (!canReceive(fluid)) {
            return 0;
        }

        if (this.fluid.isSame(Fluids.EMPTY)) {
            this.fluid = fluid;
        }

        int filled = Math.min(capacity - amount, Math.min(maxReceive, maxFill));
        if (!simulate) {
            amount += filled;
        }
        return filled;
    }

    public int drain(Fluid fluid, int maxDrain, boolean simulate) {
        if (!canExtract(fluid)) {
            return 0;
        }

        int drained = Math.min(amount, Math.min(this.maxExtract, maxDrain));
        if (!simulate) {
            amount -= drained;
        }
        return drained;
    }

    public Fluid getFluid() {
        return amount >= 0 ? fluid : Fluids.EMPTY;
    }

    public void setFluid(Fluid fluid, int amount) {
        this.fluid = fluid;
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean canExtract(Fluid fluid) {
        return (this.fluid.isSame(fluid) || this.fluid.isSame(Fluids.EMPTY)) && maxExtract > 0;
    }

    public boolean canReceive(Fluid fluid) {
        return (this.fluid.isSame(fluid) || this.fluid.isSame(Fluids.EMPTY)) && maxReceive > 0;
    }

    public CompoundTag serialize() {
        CompoundTag tag = new CompoundTag();
        tag.putString("Fluid", Balm.getRegistries().getKey(fluid).toString());
        tag.putInt("Amount", amount);
        return tag;
    }

    public void deserialize(CompoundTag tag) {
        fluid = Balm.getRegistries().getFluid(ResourceLocation.tryParse(tag.getString("Fluid")));
        amount = tag.getInt("Amount");
    }
}
