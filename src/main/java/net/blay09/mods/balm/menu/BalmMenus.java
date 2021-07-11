package net.blay09.mods.balm.menu;

import net.blay09.mods.balm.core.DeferredObject;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class BalmMenus {

    public static <T extends AbstractContainerMenu> DeferredObject<MenuType<T>> registerMenu(ResourceLocation identifier, BalmMenuFactory<T> factory) {
        return new DeferredObject<>(identifier, () -> ScreenHandlerRegistry.registerExtended(identifier, factory)).resolveImmediately();
    }
}
