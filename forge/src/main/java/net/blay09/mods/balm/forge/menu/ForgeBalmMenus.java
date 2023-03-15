package net.blay09.mods.balm.forge.menu;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.menu.BalmMenuFactory;
import net.blay09.mods.balm.api.menu.BalmMenus;
import net.blay09.mods.balm.forge.DeferredRegisters;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ForgeBalmMenus implements BalmMenus {

    @Override
    public <T extends AbstractContainerMenu> DeferredObject<MenuType<T>> registerMenu(ResourceLocation identifier, BalmMenuFactory<T> factory) {
        DeferredRegister<MenuType<?>> register = DeferredRegisters.get(ForgeRegistries.MENU_TYPES, identifier.getNamespace());
        RegistryObject<MenuType<T>> registryObject = register.register(identifier.getPath(),
                () -> new MenuType<>((IContainerFactory<T>) factory::create, FeatureFlagSet.of(FeatureFlags.VANILLA)));
        return new DeferredObject<>(identifier, registryObject, registryObject::isPresent);
    }

}
