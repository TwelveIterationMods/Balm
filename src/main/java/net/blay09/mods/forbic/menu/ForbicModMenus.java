package net.blay09.mods.forbic.menu;

import net.blay09.mods.forbic.core.DeferredObject;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class ForbicModMenus {

    public static <T extends AbstractContainerMenu> DeferredObject<MenuType<T>> registerMenu(ResourceLocation identifier, ForbicMenuFactory<T> factory) {
        return new DeferredObject<>(identifier, () -> ScreenHandlerRegistry.registerExtended(identifier, factory)).resolveImmediately();
    }
}
