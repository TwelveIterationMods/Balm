package net.blay09.mods.balm.forge.menu;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.menu.BalmMenuFactory;
import net.blay09.mods.balm.api.menu.BalmMenus;
import net.blay09.mods.balm.forge.DeferredRegisters;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ForgeBalmMenus implements BalmMenus {

    @Override
    public <T extends AbstractContainerMenu> DeferredObject<MenuType<T>> registerMenu(ResourceLocation identifier, BalmMenuFactory<T> factory) {
        DeferredRegister<MenuType<?>> register = DeferredRegisters.get(ForgeRegistries.CONTAINERS, identifier.getNamespace());
        RegistryObject<MenuType<T>> registryObject = register.register(identifier.getPath(), () -> new MenuType<>(factory));
        return new DeferredObject<>(identifier, registryObject);
    }

}
