package net.blay09.mods.forbic;

import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class ForbicModScreens {
    public static <T extends AbstractContainerMenu, S extends Screen & MenuAccess<T>> void register(MenuType<? extends T> type, ForbicScreenFactory<T, S> screenFactory) {
        ScreenRegistry.register(type, screenFactory::create);
    }

    @FunctionalInterface
    public interface ForbicScreenFactory<T, S> {
        S create(T menu, Inventory inventory, Component title);
    }
}
