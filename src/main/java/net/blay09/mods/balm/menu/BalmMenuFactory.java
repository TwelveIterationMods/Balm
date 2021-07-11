package net.blay09.mods.balm.menu;

import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.world.inventory.AbstractContainerMenu;

public interface BalmMenuFactory<T extends AbstractContainerMenu> extends ScreenHandlerRegistry.ExtendedClientHandlerFactory<T> {
}
