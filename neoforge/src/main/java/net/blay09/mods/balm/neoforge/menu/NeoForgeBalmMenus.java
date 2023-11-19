package net.blay09.mods.balm.neoforge.menu;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.menu.BalmMenuFactory;
import net.blay09.mods.balm.api.menu.BalmMenus;
import net.blay09.mods.balm.neoforge.DeferredRegisters;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.network.IContainerFactory;

public class NeoForgeBalmMenus implements BalmMenus {

    @Override
    public <T extends AbstractContainerMenu> DeferredObject<MenuType<T>> registerMenu(ResourceLocation identifier, BalmMenuFactory<T> factory) {
        final var register = DeferredRegisters.get(Registries.MENU, identifier.getNamespace());
        final var registryObject = register.register(identifier.getPath(),
                () -> new MenuType<>((IContainerFactory<T>) factory::create, FeatureFlagSet.of(FeatureFlags.VANILLA)));
        return new DeferredObject<>(identifier, registryObject, registryObject::isBound);
    }

}
