package net.blay09.mods.forbic.menu;

import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class ForbicModMenus {

    protected static <T extends AbstractContainerMenu> MenuType<T> registerMenu(ResourceLocation identifier, ForbicMenuFactory<T> factory) {
        return ScreenHandlerRegistry.registerExtended(identifier, factory);
    }
}
