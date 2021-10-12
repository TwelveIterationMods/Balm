package net.blay09.mods.balm.forge.event;


import net.blay09.mods.balm.api.event.*;
import net.blay09.mods.balm.api.event.server.ServerStartedEvent;
import net.blay09.mods.balm.api.event.server.ServerStoppedEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmllegacy.server.ServerLifecycleHooks;
import net.minecraftforge.fmlserverevents.FMLServerStartedEvent;
import net.minecraftforge.fmlserverevents.FMLServerStoppedEvent;

public class ForgeBalmCommonEvents {

    public static void registerEvents(ForgeBalmEvents events) {
        events.registerTickEvent(TickType.Server, TickPhase.Start, (ServerTickHandler handler) -> {
            MinecraftForge.EVENT_BUS.addListener((TickEvent.ServerTickEvent orig) -> {
                if (orig.phase == TickEvent.Phase.START) {
                    handler.handle(ServerLifecycleHooks.getCurrentServer());
                }
            });
        });
        events.registerTickEvent(TickType.Server, TickPhase.End, (ServerTickHandler handler) -> {
            MinecraftForge.EVENT_BUS.addListener((TickEvent.ServerTickEvent orig) -> {
                if (orig.phase == TickEvent.Phase.END) {
                    handler.handle(ServerLifecycleHooks.getCurrentServer());
                }
            });
        });
        events.registerTickEvent(TickType.ServerLevel, TickPhase.Start, (ServerLevelTickHandler handler) -> {
            MinecraftForge.EVENT_BUS.addListener((TickEvent.WorldTickEvent orig) -> {
                if (orig.phase == TickEvent.Phase.START && orig.side == LogicalSide.SERVER) {
                    handler.handle(orig.world);
                }
            });
        });
        events.registerTickEvent(TickType.ServerLevel, TickPhase.End, (ServerLevelTickHandler handler) -> {
            MinecraftForge.EVENT_BUS.addListener((TickEvent.WorldTickEvent orig) -> {
                if (orig.phase == TickEvent.Phase.END && orig.side == LogicalSide.SERVER) {
                    handler.handle(orig.world);
                }
            });
        });

        events.registerTickEvent(TickType.ServerPlayer, TickPhase.Start, (ServerPlayerTickHandler handler) -> {
            MinecraftForge.EVENT_BUS.addListener((TickEvent.PlayerTickEvent orig) -> {
                if (orig.phase == TickEvent.Phase.START && orig.side == LogicalSide.SERVER) {
                    handler.handle(((ServerPlayer) orig.player));
                }
            });
        });

        events.registerTickEvent(TickType.ServerPlayer, TickPhase.End, (ServerPlayerTickHandler handler) -> {
            MinecraftForge.EVENT_BUS.addListener((TickEvent.PlayerTickEvent orig) -> {
                if (orig.phase == TickEvent.Phase.END && orig.side == LogicalSide.SERVER) {
                    handler.handle(((ServerPlayer) orig.player));
                }
            });
        });

        events.registerEvent(ServerStartedEvent.class, priority -> {
            MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), (FMLServerStartedEvent orig) -> {
                final ServerStartedEvent event = new ServerStartedEvent(orig.getServer());
                events.fireEventHandlers(priority, event);
            });
        });

        events.registerEvent(ServerStoppedEvent.class, priority -> {
            MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), (FMLServerStoppedEvent orig) -> {
                final ServerStoppedEvent event = new ServerStoppedEvent(orig.getServer());
                events.fireEventHandlers(priority, event);
            });
        });

        events.registerEvent(UseBlockEvent.class, priority -> {
            MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), (PlayerInteractEvent.RightClickBlock orig) -> {
                final UseBlockEvent event = new UseBlockEvent(orig.getPlayer(), orig.getWorld(), orig.getHand(), orig.getHitVec());
                events.fireEventHandlers(priority, event);
                if (event.isCanceled()) {
                    orig.setCancellationResult(event.getInteractionResult());
                    orig.setCanceled(true);
                }
            });
        });

        events.registerEvent(PlayerLoginEvent.class, priority -> {
            MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), (PlayerEvent.PlayerLoggedInEvent orig) -> {
                final PlayerLoginEvent event = new PlayerLoginEvent((ServerPlayer) orig.getPlayer());
                events.fireEventHandlers(priority, event);
            });
        });

        events.registerEvent(BreakBlockEvent.Pre.class, priority -> {
            MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), (BlockEvent.BreakEvent orig) -> {
                BlockEntity blockEntity = orig.getWorld().getBlockEntity(orig.getPos());
                final BreakBlockEvent.Pre event = new BreakBlockEvent.Pre((Level) orig.getWorld(), orig.getPlayer(), orig.getPos(), orig.getState(), blockEntity);
                events.fireEventHandlers(priority, event);
                orig.setCanceled(event.isCanceled());
            });
        });

        // TODO in this case should probably fuck the common event and use platform specific code, don't wanna deal with weird bugs stemming from this
        events.registerEvent(BreakBlockEvent.Post.class, priority -> {
            // Forge has no PostBreakBlock event, so just use EventPriority lowest to run as late as possible. This will definitely be fine. :)
            MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST, (BlockEvent.BreakEvent orig) -> {
                BlockEntity blockEntity = orig.getWorld().getBlockEntity(orig.getPos());
                final BreakBlockEvent.Post event = new BreakBlockEvent.Post((Level) orig.getWorld(), orig.getPlayer(), orig.getPos(), orig.getState(), blockEntity);
                events.fireEventHandlers(priority, event);
            });
        });

        events.registerEvent(PlayerRespawnEvent.class, priority -> {
            MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), (PlayerEvent.PlayerRespawnEvent orig) -> {
                final PlayerRespawnEvent event = new PlayerRespawnEvent(((ServerPlayer) orig.getPlayer()), (ServerPlayer) orig.getPlayer());
                events.fireEventHandlers(priority, event);
            });
        });
    }

}
