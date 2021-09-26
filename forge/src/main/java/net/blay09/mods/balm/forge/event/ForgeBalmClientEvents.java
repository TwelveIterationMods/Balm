package net.blay09.mods.balm.forge.event;

import net.blay09.mods.balm.api.event.ForgeBalmEvents;
import net.blay09.mods.balm.api.event.TickPhase;
import net.blay09.mods.balm.api.event.TickType;
import net.blay09.mods.balm.api.event.client.*;
import net.blay09.mods.balm.api.event.client.screen.ScreenDrawEvent;
import net.blay09.mods.balm.api.event.client.screen.ScreenInitEvent;
import net.blay09.mods.balm.api.event.client.screen.ScreenKeyEvent;
import net.blay09.mods.balm.api.event.client.screen.ScreenMouseEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

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

        events.registerEvent(ClientStartedEvent.class, () -> {
            FMLJavaModLoadingContext.get().getModEventBus().addListener((FMLLoadCompleteEvent orig) -> {
                final ClientStartedEvent event = new ClientStartedEvent(Minecraft.getInstance());
                events.fireEventHandlers(event);
            });
        });

        events.registerEvent(ConnectedToServerEvent.class, () -> {
            MinecraftForge.EVENT_BUS.addListener((ClientPlayerNetworkEvent.LoggedInEvent orig) -> {
                final ConnectedToServerEvent event = new ConnectedToServerEvent(Minecraft.getInstance());
                events.fireEventHandlers(event);
            });
        });

        events.registerEvent(ScreenInitEvent.Pre.class, () -> {
            MinecraftForge.EVENT_BUS.addListener((GuiScreenEvent.InitGuiEvent.Pre orig) -> {
                final ScreenInitEvent.Pre event = new ScreenInitEvent.Pre(orig.getGui());
                events.fireEventHandlers(event);
            });
        });

        events.registerEvent(ScreenInitEvent.Post.class, () -> {
            MinecraftForge.EVENT_BUS.addListener((GuiScreenEvent.InitGuiEvent.Post orig) -> {
                final ScreenInitEvent.Post event = new ScreenInitEvent.Post(orig.getGui());
                events.fireEventHandlers(event);
            });
        });

        events.registerEvent(ScreenDrawEvent.Pre.class, () -> {
            MinecraftForge.EVENT_BUS.addListener((GuiScreenEvent.DrawScreenEvent.Pre orig) -> {
                final ScreenDrawEvent.Pre event = new ScreenDrawEvent.Pre(orig.getGui(), orig.getMatrixStack(), orig.getMouseX(), orig.getMouseY(), orig.getRenderPartialTicks());
                events.fireEventHandlers(event);
            });
        });

        events.registerEvent(ScreenDrawEvent.Post.class, () -> {
            MinecraftForge.EVENT_BUS.addListener((GuiScreenEvent.DrawScreenEvent.Post orig) -> {
                final ScreenDrawEvent.Post event = new ScreenDrawEvent.Post(orig.getGui(), orig.getMatrixStack(), orig.getMouseX(), orig.getMouseY(), orig.getRenderPartialTicks());
                events.fireEventHandlers(event);
            });
        });

        events.registerEvent(ScreenMouseEvent.Click.Pre.class, () -> {
            MinecraftForge.EVENT_BUS.addListener((GuiScreenEvent.MouseClickedEvent.Pre orig) -> {
                final ScreenMouseEvent.Click.Pre event = new ScreenMouseEvent.Click.Pre(orig.getGui(), orig.getMouseX(), orig.getMouseY(), orig.getButton());
                events.fireEventHandlers(event);
            });
        });

        events.registerEvent(ScreenMouseEvent.Click.Post.class, () -> {
            MinecraftForge.EVENT_BUS.addListener((GuiScreenEvent.MouseClickedEvent.Post orig) -> {
                final ScreenMouseEvent.Click.Post event = new ScreenMouseEvent.Click.Post(orig.getGui(), orig.getMouseX(), orig.getMouseY(), orig.getButton());
                events.fireEventHandlers(event);
            });
        });

        events.registerEvent(ScreenMouseEvent.Release.Pre.class, () -> {
            MinecraftForge.EVENT_BUS.addListener((GuiScreenEvent.MouseReleasedEvent.Pre orig) -> {
                final ScreenMouseEvent.Release.Pre event = new ScreenMouseEvent.Release.Pre(orig.getGui(), orig.getMouseX(), orig.getMouseY(), orig.getButton());
                events.fireEventHandlers(event);
            });
        });

        events.registerEvent(ScreenMouseEvent.Release.Post.class, () -> {
            MinecraftForge.EVENT_BUS.addListener((GuiScreenEvent.MouseReleasedEvent.Post orig) -> {
                final ScreenMouseEvent.Release.Post event = new ScreenMouseEvent.Release.Post(orig.getGui(), orig.getMouseX(), orig.getMouseY(), orig.getButton());
                events.fireEventHandlers(event);
            });
        });

        events.registerEvent(ScreenKeyEvent.Press.Pre.class, () -> {
            MinecraftForge.EVENT_BUS.addListener((GuiScreenEvent.KeyboardKeyPressedEvent.Pre orig) -> {
                final ScreenKeyEvent.Press.Pre event = new ScreenKeyEvent.Press.Pre(orig.getGui(), orig.getKeyCode(), orig.getScanCode(), orig.getModifiers());
                events.fireEventHandlers(event);
            });
        });

        events.registerEvent(ScreenKeyEvent.Press.Post.class, () -> {
            MinecraftForge.EVENT_BUS.addListener((GuiScreenEvent.KeyboardKeyPressedEvent.Post orig) -> {
                final ScreenKeyEvent.Press.Post event = new ScreenKeyEvent.Press.Post(orig.getGui(), orig.getKeyCode(), orig.getScanCode(), orig.getModifiers());
                events.fireEventHandlers(event);
            });
        });

        events.registerEvent(ScreenKeyEvent.Release.Pre.class, () -> {
            MinecraftForge.EVENT_BUS.addListener((GuiScreenEvent.KeyboardKeyReleasedEvent.Pre orig) -> {
                final ScreenKeyEvent.Release.Pre event = new ScreenKeyEvent.Release.Pre(orig.getGui(), orig.getKeyCode(), orig.getScanCode(), orig.getModifiers());
                events.fireEventHandlers(event);
            });
        });

        events.registerEvent(ScreenKeyEvent.Release.Post.class, () -> {
            MinecraftForge.EVENT_BUS.addListener((GuiScreenEvent.KeyboardKeyReleasedEvent.Post orig) -> {
                final ScreenKeyEvent.Release.Post event = new ScreenKeyEvent.Release.Post(orig.getGui(), orig.getKeyCode(), orig.getScanCode(), orig.getModifiers());
                events.fireEventHandlers(event);
            });
        });

        events.registerEvent(FovUpdateEvent.class, () -> {
            MinecraftForge.EVENT_BUS.addListener((FOVUpdateEvent orig) -> {
                final FovUpdateEvent event = new FovUpdateEvent(orig.getEntity());
                events.fireEventHandlers(event);
                if (event.getFov() != null) {
                    orig.setNewfov(event.getFov());
                }
            });
        });
    }

}
