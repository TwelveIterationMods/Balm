package net.blay09.mods.balm.forge.client.screen;

import com.mojang.datafixers.util.Pair;
import net.blay09.mods.balm.api.client.screen.BalmScreenFactory;
import net.blay09.mods.balm.api.client.screen.BalmScreens;
import net.blay09.mods.balm.forge.client.rendering.ForgeBalmRenderers;
import net.blay09.mods.balm.mixin.ScreenAccessor;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ForgeBalmScreens implements BalmScreens {

    private static class Registrations {
        public final List<Pair<Supplier<MenuType<?>>, BalmScreenFactory<?, ?>>> menuTypes = new ArrayList<>();

        @SubscribeEvent
        @SuppressWarnings({"rawtypes", "unchecked"})
        public void setupClient(FMLClientSetupEvent event) {
            for (Pair<Supplier<MenuType<?>>, BalmScreenFactory<?, ?>> entry : menuTypes) {
                registerScreenImmediate(entry.getFirst()::get, (BalmScreenFactory) entry.getSecond()); // I hate Java generics.
            }
        }
    }

    private final Map<String, Registrations> registrations = new HashMap<>();

    @Override
    public <T extends AbstractContainerMenu, S extends Screen & MenuAccess<T>> void registerScreen(Supplier<MenuType<? extends T>> type, BalmScreenFactory<T, S> screenFactory) {
        getActiveRegistrations().menuTypes.add(Pair.of(type::get, screenFactory));
    }

    private static <T extends AbstractContainerMenu, S extends Screen & MenuAccess<T>> void registerScreenImmediate(Supplier<MenuType<? extends T>> type, BalmScreenFactory<T, S> screenFactory) {
        MenuScreens.register(type.get(), screenFactory::create);
    }

    @Override
    public AbstractWidget addRenderableWidget(Screen screen, AbstractWidget widget) {
        ScreenAccessor accessor = ((ScreenAccessor) screen);
        accessor.getChildren().add(widget);
        accessor.getRenderables().add(widget);
        accessor.getNarratables().add(widget);
        return widget;
    }

    public void register() {
        FMLJavaModLoadingContext.get().getModEventBus().register(getActiveRegistrations());
    }

    private Registrations getActiveRegistrations() {
        return registrations.computeIfAbsent(ModLoadingContext.get().getActiveNamespace(), it -> new Registrations());
    }
}
