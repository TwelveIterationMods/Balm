package net.blay09.mods.balm.forge.item;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.item.BalmItems;
import net.blay09.mods.balm.forge.DeferredRegisters;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ForgeBalmItems implements BalmItems {
    @Override
    public Item.Properties itemProperties(CreativeModeTab creativeModeTab) {
        return new Item.Properties().tab(creativeModeTab);
    }

    @Override
    public DeferredObject<Item> registerItem(Supplier<Item> supplier, ResourceLocation identifier) {
        DeferredRegister<Item> register = DeferredRegisters.get(ForgeRegistries.ITEMS, identifier.getNamespace());
        RegistryObject<Item> registryObject = register.register(identifier.getPath(), supplier);
        return new DeferredObject<>(identifier, registryObject, registryObject::isPresent);
    }

    @Override

    public CreativeModeTab createCreativeModeTab(ResourceLocation identifier, Supplier<ItemStack> iconSupplier) {
        return new CreativeModeTab(identifier.toString().replace(':', '.')) {
            @Override
            public ItemStack makeIcon() {
                return iconSupplier.get();
            }
        };
    }

}
