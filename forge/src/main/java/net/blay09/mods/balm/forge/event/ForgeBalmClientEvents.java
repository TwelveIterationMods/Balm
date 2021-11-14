package net.blay09.mods.balm.forge.event;

import net.blay09.mods.balm.api.event.ForgeBalmEvents;
import net.blay09.mods.balm.api.event.client.RecipesUpdatedEvent;
import net.blay09.mods.balm.api.event.TickPhase;
import net.blay09.mods.balm.api.event.TickType;
import net.blay09.mods.balm.api.event.client.*;
import net.blay09.mods.balm.api.event.client.RenderHandEvent;
import net.blay09.mods.balm.api.event.client.screen.*;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.Nullable;

public class ForgeBalmClientEvents {

    public static void registerEvents(ForgeBalmEvents events) {
        events.registerTickEvent(TickType.Client, TickPhase.Start, (ClientTickHandler handler) -> {
            MinecraftForge.EVENT_BUS.addListener((TickEvent.ClientTickEvent orig) -> {
                if (orig.phase == TickEvent.Phase.START) {
                    handler.handle(Minecraft.getInstance());
                }
            });
        });
        events.registerTickEvent(TickType.Client, TickPhase.End, (ClientTickHandler handler) -> {
            MinecraftForge.EVENT_BUS.addListener((TickEvent.ClientTickEvent orig) -> {
                if (orig.phase == TickEvent.Phase.END) {
                    handler.handle(Minecraft.getInstance());
                }
            });
        });
        events.registerTickEvent(TickType.ClientLevel, TickPhase.Start, (ClientLevelTickHandler handler) -> {
            MinecraftForge.EVENT_BUS.addListener((TickEvent.ClientTickEvent orig) -> {
                if (orig.phase == TickEvent.Phase.START) {
                    handler.handle(Minecraft.getInstance().level);
                }
            });
        });
        events.registerTickEvent(TickType.ClientLevel, TickPhase.End, (ClientLevelTickHandler handler) -> {
            MinecraftForge.EVENT_BUS.addListener((TickEvent.ClientTickEvent orig) -> {
                if (orig.phase == TickEvent.Phase.END) {
                    handler.handle(Minecraft.getInstance().level);
                }
            });
        });

        events.registerEvent(ClientStartedEvent.class, priority -> {
            FMLJavaModLoadingContext.get().getModEventBus().addListener(ForgeBalmEvents.toForge(priority), (FMLLoadCompleteEvent orig) -> {
                final ClientStartedEvent event = new ClientStartedEvent(Minecraft.getInstance());
                events.fireEventHandlers(priority, event);
            });
        });

        events.registerEvent(ConnectedToServerEvent.class, priority -> {
            MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), (ClientPlayerNetworkEvent.LoggedInEvent orig) -> {
                final ConnectedToServerEvent event = new ConnectedToServerEvent(Minecraft.getInstance());
                events.fireEventHandlers(priority, event);
            });
        });

        events.registerEvent(ScreenInitEvent.Pre.class, priority -> {
            MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), (GuiScreenEvent.InitGuiEvent.Pre orig) -> {
                final ScreenInitEvent.Pre event = new ScreenInitEvent.Pre(orig.getGui());
                events.fireEventHandlers(priority, event);
                if (event.isCanceled()) {
                    orig.setCanceled(true);
                }
            });
        });

        events.registerEvent(ScreenInitEvent.Post.class, priority -> {
            MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), (GuiScreenEvent.InitGuiEvent.Post orig) -> {
                final ScreenInitEvent.Post event = new ScreenInitEvent.Post(orig.getGui());
                events.fireEventHandlers(priority, event);
            });
        });

        events.registerEvent(ScreenDrawEvent.Pre.class, priority -> {
            MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), (GuiScreenEvent.DrawScreenEvent.Pre orig) -> {
                final ScreenDrawEvent.Pre event = new ScreenDrawEvent.Pre(orig.getGui(), orig.getMatrixStack(), orig.getMouseX(), orig.getMouseY(), orig.getRenderPartialTicks());
                events.fireEventHandlers(priority, event);
                if (event.isCanceled()) {
                    orig.setCanceled(true);
                }
            });
        });

        events.registerEvent(ContainerScreenDrawEvent.Background.class, priority -> {
            MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), (GuiContainerEvent.DrawBackground orig) -> {
                final ContainerScreenDrawEvent.Background event = new ContainerScreenDrawEvent.Background(orig.getGuiContainer(), orig.getMatrixStack(), orig.getMouseX(), orig.getMouseY());
                events.fireEventHandlers(priority, event);
            });
        });

        events.registerEvent(ContainerScreenDrawEvent.Foreground.class, priority -> {
            MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), (GuiContainerEvent.DrawBackground orig) -> {
                final ContainerScreenDrawEvent.Foreground event = new ContainerScreenDrawEvent.Foreground(orig.getGuiContainer(), orig.getMatrixStack(), orig.getMouseX(), orig.getMouseY());
                events.fireEventHandlers(priority, event);
            });
        });

        events.registerEvent(ScreenDrawEvent.Post.class, priority -> {
            MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), (GuiScreenEvent.DrawScreenEvent.Post orig) -> {
                final ScreenDrawEvent.Post event = new ScreenDrawEvent.Post(orig.getGui(), orig.getMatrixStack(), orig.getMouseX(), orig.getMouseY(), orig.getRenderPartialTicks());
                events.fireEventHandlers(priority, event);
            });
        });

        events.registerEvent(ScreenMouseEvent.Click.Pre.class, priority -> {
            MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), (GuiScreenEvent.MouseClickedEvent.Pre orig) -> {
                final ScreenMouseEvent.Click.Pre event = new ScreenMouseEvent.Click.Pre(orig.getGui(), orig.getMouseX(), orig.getMouseY(), orig.getButton());
                events.fireEventHandlers(priority, event);
                if (event.isCanceled()) {
                    orig.setCanceled(true);
                }
            });
        });

        events.registerEvent(ScreenMouseEvent.Click.Post.class, priority -> {
            MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), (GuiScreenEvent.MouseClickedEvent.Post orig) -> {
                final ScreenMouseEvent.Click.Post event = new ScreenMouseEvent.Click.Post(orig.getGui(), orig.getMouseX(), orig.getMouseY(), orig.getButton());
                events.fireEventHandlers(priority, event);
                if (event.isCanceled()) {
                    orig.setCanceled(true);
                }
            });
        });

        events.registerEvent(ScreenMouseEvent.Drag.Pre.class, priority -> {
            MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), (GuiScreenEvent.MouseDragEvent.Pre orig) -> {
                final ScreenMouseEvent.Drag.Pre event = new ScreenMouseEvent.Drag.Pre(orig.getGui(), orig.getMouseX(), orig.getMouseY(), orig.getMouseButton(), orig.getDragX(), orig.getDragY());
                events.fireEventHandlers(priority, event);
                if (event.isCanceled()) {
                    orig.setCanceled(true);
                }
            });
        });

        events.registerEvent(ScreenMouseEvent.Drag.Post.class, priority -> {
            MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), (GuiScreenEvent.MouseDragEvent.Post orig) -> {
                final ScreenMouseEvent.Drag.Post event = new ScreenMouseEvent.Drag.Post(orig.getGui(), orig.getMouseX(), orig.getMouseY(), orig.getMouseButton(), orig.getDragX(), orig.getDragY());
                events.fireEventHandlers(priority, event);
                if (event.isCanceled()) {
                    orig.setCanceled(true);
                }
            });
        });

        events.registerEvent(ScreenMouseEvent.Release.Pre.class, priority -> {
            MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), (GuiScreenEvent.MouseReleasedEvent.Pre orig) -> {
                final ScreenMouseEvent.Release.Pre event = new ScreenMouseEvent.Release.Pre(orig.getGui(), orig.getMouseX(), orig.getMouseY(), orig.getButton());
                events.fireEventHandlers(priority, event);
                if (event.isCanceled()) {
                    orig.setCanceled(true);
                }
            });
        });

        events.registerEvent(ScreenMouseEvent.Release.Post.class, priority -> {
            MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), (GuiScreenEvent.MouseReleasedEvent.Post orig) -> {
                final ScreenMouseEvent.Release.Post event = new ScreenMouseEvent.Release.Post(orig.getGui(), orig.getMouseX(), orig.getMouseY(), orig.getButton());
                events.fireEventHandlers(priority, event);
                if (event.isCanceled()) {
                    orig.setCanceled(true);
                }
            });
        });

        events.registerEvent(ScreenKeyEvent.Press.Pre.class, priority -> {
            MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), (GuiScreenEvent.KeyboardKeyPressedEvent.Pre orig) -> {
                final ScreenKeyEvent.Press.Pre event = new ScreenKeyEvent.Press.Pre(orig.getGui(), orig.getKeyCode(), orig.getScanCode(), orig.getModifiers());
                events.fireEventHandlers(priority, event);
                if (event.isCanceled()) {
                    orig.setCanceled(true);
                }
            });
        });

        events.registerEvent(ScreenKeyEvent.Press.Post.class, priority -> {
            MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), (GuiScreenEvent.KeyboardKeyPressedEvent.Post orig) -> {
                final ScreenKeyEvent.Press.Post event = new ScreenKeyEvent.Press.Post(orig.getGui(), orig.getKeyCode(), orig.getScanCode(), orig.getModifiers());
                events.fireEventHandlers(priority, event);
                if (event.isCanceled()) {
                    orig.setCanceled(true);
                }
            });
        });

        events.registerEvent(ScreenKeyEvent.Release.Pre.class, priority -> {
            MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), (GuiScreenEvent.KeyboardKeyReleasedEvent.Pre orig) -> {
                final ScreenKeyEvent.Release.Pre event = new ScreenKeyEvent.Release.Pre(orig.getGui(), orig.getKeyCode(), orig.getScanCode(), orig.getModifiers());
                events.fireEventHandlers(priority, event);
                if (event.isCanceled()) {
                    orig.setCanceled(true);
                }
            });
        });

        events.registerEvent(ScreenKeyEvent.Release.Post.class, priority -> {
            MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), (GuiScreenEvent.KeyboardKeyReleasedEvent.Post orig) -> {
                final ScreenKeyEvent.Release.Post event = new ScreenKeyEvent.Release.Post(orig.getGui(), orig.getKeyCode(), orig.getScanCode(), orig.getModifiers());
                events.fireEventHandlers(priority, event);
                if (event.isCanceled()) {
                    orig.setCanceled(true);
                }
            });
        });

        events.registerEvent(FovUpdateEvent.class, priority -> {
            MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), (FOVUpdateEvent orig) -> {
                final FovUpdateEvent event = new FovUpdateEvent(orig.getEntity());
                events.fireEventHandlers(priority, event);
                if (event.getFov() != null) {
                    orig.setNewfov(event.getFov());
                }
            });
        });

        events.registerEvent(RecipesUpdatedEvent.class, priority -> {
            MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), (net.minecraftforge.client.event.RecipesUpdatedEvent orig) -> {
                final RecipesUpdatedEvent event = new RecipesUpdatedEvent(orig.getRecipeManager());
                events.fireEventHandlers(priority, event);
            });
        });

        events.registerEvent(ItemTooltipEvent.class, priority -> {
            MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), (net.minecraftforge.event.entity.player.ItemTooltipEvent orig) -> {
                final ItemTooltipEvent event = new ItemTooltipEvent(orig.getItemStack(), orig.getPlayer(), orig.getToolTip(), orig.getFlags());
                events.fireEventHandlers(priority, event);
            });
        });

        events.registerEvent(PotionShiftEvent.class, priority -> {
            MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), (GuiScreenEvent.PotionShiftEvent orig) -> {
                final PotionShiftEvent event = new PotionShiftEvent(orig.getGui());
                events.fireEventHandlers(priority, event);
                if (event.isCanceled()) {
                    orig.setCanceled(true);
                }
            });
        });

        events.registerEvent(UseItemInputEvent.class, priority -> {
            MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), (InputEvent.ClickInputEvent orig) -> {
                if (orig.isUseItem()) {
                    final UseItemInputEvent event = new UseItemInputEvent(orig.getHand());
                    events.fireEventHandlers(priority, event);
                    if (event.isCanceled()) {
                        orig.setSwingHand(false);
                        orig.setCanceled(true);
                    }
                }
            });
        });

        events.registerEvent(RenderHandEvent.class, priority -> {
            MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), (net.minecraftforge.client.event.RenderHandEvent orig) -> {
                final RenderHandEvent event = new RenderHandEvent(orig.getHand(), orig.getItemStack(), orig.getSwingProgress());
                events.fireEventHandlers(priority, event);
                if (event.isCanceled()) {
                    orig.setCanceled(true);
                }
            });
        });

        events.registerEvent(KeyInputEvent.class, priority -> {
            MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), (InputEvent.KeyInputEvent orig) -> {
                final KeyInputEvent event = new KeyInputEvent(orig.getKey(), orig.getScanCode(), orig.getAction(), orig.getModifiers());
                events.fireEventHandlers(priority, event);
            });
        });

        events.registerEvent(BlockHighlightDrawEvent.class, priority -> {
            MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), (DrawSelectionEvent.HighlightBlock orig) -> {
                final BlockHighlightDrawEvent event = new BlockHighlightDrawEvent(orig.getTarget(), orig.getMatrix(), orig.getBuffers(), orig.getInfo());
                events.fireEventHandlers(priority, event);
                if (event.isCanceled()) {
                    orig.setCanceled(true);
                }
            });
        });

        events.registerEvent(OpenScreenEvent.class, priority -> {
            MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), (GuiOpenEvent orig) -> {
                final OpenScreenEvent event = new OpenScreenEvent(orig.getGui());
                events.fireEventHandlers(priority, event);
                orig.setGui(event.getScreen());
                if (event.isCanceled()) {
                    orig.setCanceled(true);
                }
            });
        });

        events.registerEvent(GuiDrawEvent.Pre.class, priority -> {
            MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), (RenderGameOverlayEvent.Pre orig) -> {
                GuiDrawEvent.Element type = getGuiDrawEventElement(orig);
                if (type != null) {
                    final GuiDrawEvent.Pre event = new GuiDrawEvent.Pre(orig.getWindow(), orig.getMatrixStack(), type);
                    events.fireEventHandlers(priority, event);
                    if (event.isCanceled()) {
                        orig.setCanceled(true);
                    }
                }
            });
        });

        events.registerEvent(GuiDrawEvent.Post.class, priority -> {
            MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), (RenderGameOverlayEvent.Post orig) -> {
                GuiDrawEvent.Element type = getGuiDrawEventElement(orig);
                if (type != null) {
                    final GuiDrawEvent.Post event = new GuiDrawEvent.Post(orig.getWindow(), orig.getMatrixStack(), type);
                    events.fireEventHandlers(priority, event);
                }
            });
        });
    }

    @Nullable
    private static GuiDrawEvent.Element getGuiDrawEventElement(RenderGameOverlayEvent orig) {
        GuiDrawEvent.Element type = null;
        IIngameOverlay overlay = null;
        if (orig instanceof RenderGameOverlayEvent.PreLayer preLayer) {
            overlay = preLayer.getOverlay();
        } else if (orig instanceof RenderGameOverlayEvent.PostLayer postLayer) {
            overlay = postLayer.getOverlay();
        }

        if(overlay != null) {
            if (overlay == ForgeIngameGui.PLAYER_HEALTH_ELEMENT) {
                type = GuiDrawEvent.Element.HEALTH;
            }
        } else {
            type = switch (orig.getType()) {
                case ALL -> GuiDrawEvent.Element.ALL;
                case CHAT -> GuiDrawEvent.Element.CHAT;
                case DEBUG -> GuiDrawEvent.Element.DEBUG;
                case BOSSINFO -> GuiDrawEvent.Element.BOSS_INFO;
                case PLAYER_LIST -> GuiDrawEvent.Element.PLAYER_LIST;
                default -> null;
            };
        }

        return type;
    }

}
