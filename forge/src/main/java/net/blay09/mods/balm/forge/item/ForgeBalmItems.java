package net.blay09.mods.balm.forge.item;

import com.google.common.collect.*;
import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.item.BalmItems;
import net.blay09.mods.balm.forge.DeferredRegisters;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
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

    private static class CreativeTabRegistration extends DeferredObject<CreativeModeTab> {
        private final Component displayName;
        private final Supplier<ItemStack> iconSupplier;

        private CreativeTabRegistration(
                ResourceLocation identifier,
                Component displayName,
                Supplier<ItemStack> iconSupplier
        ) {
            super(identifier);
            this.displayName = displayName;
            this.iconSupplier = iconSupplier;
        }

        public void resolve(CreativeModeTabEvent.Register event) {
            set(event.registerCreativeModeTab(getIdentifier(), builder -> builder
                    .title(displayName)
                    .icon(iconSupplier)));
        }
    }


    private static class Registrations {
        public final List<CreativeTabRegistration> creativeTabsToRegister = new ArrayList<>();
        public final Multimap<ResourceLocation, Supplier<ItemLike[]>> creativeTabContents = ArrayListMultimap.create();
        private final BiMap<ResourceLocation, CreativeModeTab> creativeTabsByIdentifier = HashBiMap.create();
        private final BiMap<CreativeModeTab, ResourceLocation> creativeTabIdentifiersByTab = creativeTabsByIdentifier.inverse();

        @SubscribeEvent
        public void registerCreativeTabs(CreativeModeTabEvent.Register event) {
            creativeTabsToRegister.forEach(it -> {
                it.resolve(event);
                creativeTabsByIdentifier.put(it.getIdentifier(), it.get());
            });

            creativeTabsByIdentifier.put(new ResourceLocation("minecraft", "building_blocks"), CreativeModeTabs.BUILDING_BLOCKS);
            creativeTabsByIdentifier.put(new ResourceLocation("minecraft", "spawn_eggs"), CreativeModeTabs.SPAWN_EGGS);
            creativeTabsByIdentifier.put(new ResourceLocation("minecraft", "colored_blocks"), CreativeModeTabs.COLORED_BLOCKS);
            creativeTabsByIdentifier.put(new ResourceLocation("minecraft", "combat"), CreativeModeTabs.COMBAT);
            creativeTabsByIdentifier.put(new ResourceLocation("minecraft", "food_and_drinks"), CreativeModeTabs.FOOD_AND_DRINKS);
            creativeTabsByIdentifier.put(new ResourceLocation("minecraft", "functional_blocks"), CreativeModeTabs.FUNCTIONAL_BLOCKS);
            creativeTabsByIdentifier.put(new ResourceLocation("minecraft", "ingredients"), CreativeModeTabs.INGREDIENTS);
            creativeTabsByIdentifier.put(new ResourceLocation("minecraft", "natural_blocks"), CreativeModeTabs.NATURAL_BLOCKS);
            creativeTabsByIdentifier.put(new ResourceLocation("minecraft", "redstone_blocks"), CreativeModeTabs.REDSTONE_BLOCKS);
            creativeTabsByIdentifier.put(new ResourceLocation("minecraft", "op_blocks"), CreativeModeTabs.OP_BLOCKS);
            creativeTabsByIdentifier.put(new ResourceLocation("minecraft", "tools_and_utilities"), CreativeModeTabs.TOOLS_AND_UTILITIES);
        }

        @SubscribeEvent
        public void buildCreativeTabContents(CreativeModeTabEvent.BuildContents event) {
            ResourceLocation tabIdentifier = creativeTabIdentifiersByTab.get(event.getTab());
            if (tabIdentifier != null) {
                Collection<Supplier<ItemLike[]>> itemStacks = creativeTabContents.get(tabIdentifier);
                if (!itemStacks.isEmpty()) {
                    itemStacks.forEach(it -> {
                        for (ItemLike itemStack : it.get()) {
                            event.accept(itemStack);
                        }
                    });
                }
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
    public DeferredObject<CreativeModeTab> registerCreativeModeTab(ResourceLocation identifier, Supplier<ItemStack> iconSupplier) {
        Component displayName = Component.translatable("itemGroup." + identifier.toString().replace(':', '.'));
        CreativeTabRegistration deferred = new CreativeTabRegistration(identifier,
                displayName,
                iconSupplier);
        getActiveRegistrations().creativeTabsToRegister.add(deferred);
        return deferred;
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
