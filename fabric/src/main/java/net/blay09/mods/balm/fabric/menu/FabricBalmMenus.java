package net.blay09.mods.balm.fabric.menu;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.menu.BalmMenus;
import net.blay09.mods.balm.api.menu.BalmMenuFactory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class FabricBalmMenus implements BalmMenus {

    @Override
    public <T extends AbstractContainerMenu> DeferredObject<MenuType<T>> registerMenu(ResourceLocation identifier, BalmMenuFactory<T> factory) {
        return new DeferredObject<>(identifier, () -> {
            // return TODO Registry.register(Registries.MENU, identifier, MenuType.ANVIL);
            return (MenuType<T>) MenuType.ANVIL;
        }).resolveImmediately();
    }

}
