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

        events.registerEvent(UseItemEvent.class, priority -> {
            MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), (PlayerInteractEvent.RightClickItem orig) -> {
                final UseItemEvent event = new UseItemEvent(orig.getPlayer(), orig.getWorld(), orig.getHand());
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

        events.registerEvent(BreakBlockEvent.class, priority -> {
            MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), (BlockEvent.BreakEvent orig) -> {
                BlockEntity blockEntity = orig.getWorld().getBlockEntity(orig.getPos());
                final BreakBlockEvent event = new BreakBlockEvent((Level) orig.getWorld(), orig.getPlayer(), orig.getPos(), orig.getState(), blockEntity);
                events.fireEventHandlers(priority, event);
                if (event.isCanceled()) {
                    orig.setCanceled(true);
                }
            });
        });

        events.registerEvent(PlayerRespawnEvent.class, priority -> {
            MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), (PlayerEvent.PlayerRespawnEvent orig) -> {
                final PlayerRespawnEvent event = new PlayerRespawnEvent(((ServerPlayer) orig.getPlayer()), (ServerPlayer) orig.getPlayer());
                events.fireEventHandlers(priority, event);
            });
        });

        events.registerEvent(LivingFallEvent.class, priority -> {
            MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), (net.minecraftforge.event.entity.living.LivingFallEvent orig) -> {
                final LivingFallEvent event = new LivingFallEvent(orig.getEntityLiving());
                events.fireEventHandlers(priority, event);
                if (event.isCanceled()) {
                    orig.setCanceled(true);
                }
            });
        });

        events.registerEvent(LivingDamageEvent.class, priority -> {
            MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), (net.minecraftforge.event.entity.living.LivingDamageEvent orig) -> {
                final LivingDamageEvent event = new LivingDamageEvent(orig.getEntityLiving(), orig.getSource(), orig.getAmount());
                events.fireEventHandlers(priority, event);
                if (event.isCanceled()) {
                    orig.setCanceled(true);
                }
            });
        });
    }

}
