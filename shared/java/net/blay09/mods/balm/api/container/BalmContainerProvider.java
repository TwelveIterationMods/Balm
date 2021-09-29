package net.blay09.mods.balm.api.container;

import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import org.jetbrains.annotations.Nullable;

public interface BalmContainerProvider {
    default Container getContainer() {
        return getContainer(null);
    }

    Container getContainer(@Nullable Direction side);
}
