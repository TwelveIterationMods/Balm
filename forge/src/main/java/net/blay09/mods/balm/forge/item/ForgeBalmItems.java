package net.blay09.mods.balm.forge.item;

import com.google.common.collect.*;
import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.item.BalmItems;
import net.blay09.mods.balm.forge.DeferredRegisters;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class ForgeBalmItems implements BalmItems {

    private static class Registrations {
        public final Multimap<ResourceLocation, Supplier<ItemLike[]>> creativeTabContents = ArrayListMultimap.create();

        public void buildCreativeTabContents(ResourceLocation tabIdentifier, CreativeModeTab.Output entries) {
            Collection<Supplier<ItemLike[]>> itemStacks = creativeTabContents.get(tabIdentifier);
            if (!itemStacks.isEmpty()) {
                itemStacks.forEach(it -> {
                    for (ItemLike itemStack : it.get()) {
                        entries.accept(itemStack);
                    }
                });
            }
        }
    }

    private final Map<String, Registrations> registrations = new ConcurrentHashMap<>();

    @Override
    public Item.Properties itemProperties() {
        return new Item.Properties();
    }

    @Override
    public DeferredObject<Item> registerItem(Supplier<Item> supplier, ResourceLocation identifier, @Nullable ResourceLocation creativeTab) {
        DeferredRegister<Item> register = DeferredRegisters.get(ForgeRegistries.ITEMS, identifier.getNamespace());
        RegistryObject<Item> registryObject = register.register(identifier.getPath(), supplier);
        if (creativeTab != null) {
            getActiveRegistrations().creativeTabContents.put(creativeTab, () -> new ItemLike[]{registryObject.get()});
        }
        return new DeferredObject<>(identifier, registryObject, registryObject::isPresent);
    }

    @Override
    public DeferredObject<CreativeModeTab> registerCreativeModeTab(Supplier<ItemStack> iconSupplier, ResourceLocation identifier) {
        DeferredRegister<CreativeModeTab> register = DeferredRegisters.get(Registries.CREATIVE_MODE_TAB, identifier.getNamespace());
        RegistryObject<CreativeModeTab> registryObject = register.register(identifier.getPath(), () -> {
            Component displayName = Component.translatable("itemGroup." + identifier.toString().replace(':', '.'));
            final var registrations = getActiveRegistrations();
            CreativeModeTab creativeModeTab = CreativeModeTab.builder()
                    .title(displayName)
                    .icon(iconSupplier)
                    .displayItems((enabledFeatures, entries) -> registrations.buildCreativeTabContents(identifier, entries))
                    .build();
            creativeModeTab = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, identifier, creativeModeTab);
            return creativeModeTab;
        });
        return new DeferredObject<>(identifier, registryObject, registryObject::isPresent);
    }

    @Override
    public void addToCreativeModeTab(ResourceLocation tabIdentifier, Supplier<ItemLike[]> itemsSupplier) {
        getActiveRegistrations().creativeTabContents.put(tabIdentifier, itemsSupplier);
    }

    public void register() {
        FMLJavaModLoadingContext.get().getModEventBus().register(getActiveRegistrations());
    }

    private Registrations getActiveRegistrations() {
        return registrations.computeIfAbsent(ModLoadingContext.get().getActiveNamespace(), it -> new Registrations());
    }
}
