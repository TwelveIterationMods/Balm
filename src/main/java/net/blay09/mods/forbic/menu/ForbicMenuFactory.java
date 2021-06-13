package net.blay09.mods.forbic.menu;

import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.world.inventory.AbstractContainerMenu;

public interface ForbicMenuFactory<T extends AbstractContainerMenu> extends ScreenHandlerRegistry.ExtendedClientHandlerFactory<T> {
}
