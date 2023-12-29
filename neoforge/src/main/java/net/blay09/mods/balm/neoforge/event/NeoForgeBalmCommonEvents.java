package net.blay09.mods.balm.neoforge.event;


import net.blay09.mods.balm.api.event.*;
import net.blay09.mods.balm.api.event.server.ServerStartedEvent;
import net.blay09.mods.balm.api.event.server.ServerStoppedEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.LogicalSide;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.TickEvent;
import net.neoforged.neoforge.event.entity.item.ItemTossEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.ChunkWatchEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

public class NeoForgeBalmCommonEvents {

    public static void registerEvents(NeoForgeBalmEvents events) {
        events.registerTickEvent(TickType.Server, TickPhase.Start, (ServerTickHandler handler) -> {
            NeoForge.EVENT_BUS.addListener((TickEvent.ServerTickEvent orig) -> {
                if (orig.phase == TickEvent.Phase.START) {
                    handler.handle(ServerLifecycleHooks.getCurrentServer());
                }
            });
        });
        events.registerTickEvent(TickType.Server, TickPhase.End, (ServerTickHandler handler) -> {
            NeoForge.EVENT_BUS.addListener((TickEvent.ServerTickEvent orig) -> {
                if (orig.phase == TickEvent.Phase.END) {
                    handler.handle(ServerLifecycleHooks.getCurrentServer());
                }
            });
        });
        events.registerTickEvent(TickType.ServerLevel, TickPhase.Start, (ServerLevelTickHandler handler) -> {
            NeoForge.EVENT_BUS.addListener((TickEvent.LevelTickEvent orig) -> {
                if (orig.phase == TickEvent.Phase.START && orig.side == LogicalSide.SERVER) {
                    handler.handle(orig.level);
                }
            });
        });
        events.registerTickEvent(TickType.ServerLevel, TickPhase.End, (ServerLevelTickHandler handler) -> {
            NeoForge.EVENT_BUS.addListener((TickEvent.LevelTickEvent orig) -> {
                if (orig.phase == TickEvent.Phase.END && orig.side == LogicalSide.SERVER) {
                    handler.handle(orig.level);
                }
            });
        });

        events.registerTickEvent(TickType.ServerPlayer, TickPhase.Start, (ServerPlayerTickHandler handler) -> {
            NeoForge.EVENT_BUS.addListener((TickEvent.PlayerTickEvent orig) -> {
                if (orig.phase == TickEvent.Phase.START && orig.side == LogicalSide.SERVER) {
                    handler.handle(((ServerPlayer) orig.player));
                }
            });
        });

        events.registerTickEvent(TickType.ServerPlayer, TickPhase.End, (ServerPlayerTickHandler handler) -> {
            NeoForge.EVENT_BUS.addListener((TickEvent.PlayerTickEvent orig) -> {
                if (orig.phase == TickEvent.Phase.END && orig.side == LogicalSide.SERVER) {
                    handler.handle(((ServerPlayer) orig.player));
                }
            });
        });

        events.registerEvent(ServerStartedEvent.class, priority -> {
            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (net.neoforged.neoforge.event.server.ServerStartedEvent orig) -> {
                final ServerStartedEvent event = new ServerStartedEvent(orig.getServer());
                events.fireEventHandlers(priority, event);
            });
        });

        events.registerEvent(ServerStoppedEvent.class, priority -> {
            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (net.neoforged.neoforge.event.server.ServerStoppedEvent orig) -> {
                final ServerStoppedEvent event = new ServerStoppedEvent(orig.getServer());
                events.fireEventHandlers(priority, event);
            });
        });

        events.registerEvent(UseBlockEvent.class, priority -> {
            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (PlayerInteractEvent.RightClickBlock orig) -> {
                final UseBlockEvent event = new UseBlockEvent(orig.getEntity(), orig.getLevel(), orig.getHand(), orig.getHitVec());
                events.fireEventHandlers(priority, event);
                if (event.isCanceled()) {
                    orig.setCancellationResult(event.getInteractionResult());
                    orig.setCanceled(true);
                }
            });
        });

        events.registerEvent(UseItemEvent.class, priority -> {
            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (PlayerInteractEvent.RightClickItem orig) -> {
                final UseItemEvent event = new UseItemEvent(orig.getEntity(), orig.getLevel(), orig.getHand());
                events.fireEventHandlers(priority, event);
                if (event.isCanceled()) {
                    orig.setCancellationResult(event.getInteractionResult());
                    orig.setCanceled(true);
                }
            });
        });

        events.registerEvent(PlayerLoginEvent.class, priority -> {
            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (PlayerEvent.PlayerLoggedInEvent orig) -> {
                final PlayerLoginEvent event = new PlayerLoginEvent((ServerPlayer) orig.getEntity());
                events.fireEventHandlers(priority, event);
            });
        });

        events.registerEvent(PlayerLogoutEvent.class, priority -> {
            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (PlayerEvent.PlayerLoggedOutEvent orig) -> {
                final PlayerLogoutEvent event = new PlayerLogoutEvent((ServerPlayer) orig.getEntity());
                events.fireEventHandlers(priority, event);
            });
        });

        events.registerEvent(BreakBlockEvent.class, priority -> {
            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (BlockEvent.BreakEvent orig) -> {
                BlockEntity blockEntity = orig.getLevel().getBlockEntity(orig.getPos());
                final BreakBlockEvent event = new BreakBlockEvent((Level) orig.getLevel(), orig.getPlayer(), orig.getPos(), orig.getState(), blockEntity);
                events.fireEventHandlers(priority, event);
                if (event.isCanceled()) {
                    orig.setCanceled(true);
                }
            });
        });

        events.registerEvent(PlayerRespawnEvent.class, priority -> {
            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (PlayerEvent.PlayerRespawnEvent orig) -> {
                final PlayerRespawnEvent event = new PlayerRespawnEvent(((ServerPlayer) orig.getEntity()), (ServerPlayer) orig.getEntity());
                events.fireEventHandlers(priority, event);
            });
        });

        events.registerEvent(LivingFallEvent.class, priority -> {
            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (net.neoforged.neoforge.event.entity.living.LivingFallEvent orig) -> {
                final LivingFallEvent event = new LivingFallEvent(orig.getEntity());
                events.fireEventHandlers(priority, event);

                if (event.getFallDamageOverride() != null) {
                    orig.setDamageMultiplier(0f);
                    event.getEntity().hurt(event.getEntity().level().damageSources().fall(), event.getFallDamageOverride());
                }

                if (event.isCanceled()) {
                    orig.setCanceled(true);
                }
            });
        });

        events.registerEvent(LivingDamageEvent.class, priority -> {
            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (net.neoforged.neoforge.event.entity.living.LivingDamageEvent orig) -> {
                final LivingDamageEvent event = new LivingDamageEvent(orig.getEntity(), orig.getSource(), orig.getAmount());
                events.fireEventHandlers(priority, event);
                orig.setAmount(event.getDamageAmount());
                if (event.isCanceled()) {
                    orig.setCanceled(true);
                }
            });
        });

        events.registerEvent(CropGrowEvent.Pre.class, priority -> {
            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (BlockEvent.CropGrowEvent.Pre orig) -> {
                if (orig.getLevel() instanceof Level level) {
                    final CropGrowEvent.Pre event = new CropGrowEvent.Pre(level, orig.getPos(), orig.getState());
                    events.fireEventHandlers(priority, event);
                    if (event.isCanceled()) {
                        orig.setResult(Event.Result.DENY);
                    }
                }
            });
        });

        events.registerEvent(CropGrowEvent.Post.class, priority -> {
            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (BlockEvent.CropGrowEvent.Post orig) -> {
                if (orig.getLevel() instanceof Level level) {
                    final CropGrowEvent.Post event = new CropGrowEvent.Post(level, orig.getPos(), orig.getState());
                    events.fireEventHandlers(priority, event);
                }
            });
        });

        events.registerEvent(ChunkTrackingEvent.Start.class, priority -> {
            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (ChunkWatchEvent.Watch orig) -> {
                final ChunkTrackingEvent.Start event = new ChunkTrackingEvent.Start(orig.getLevel(), orig.getPlayer(), orig.getPos());
                events.fireEventHandlers(priority, event);
            });
        });

        events.registerEvent(ChunkTrackingEvent.Stop.class, priority -> {
            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (ChunkWatchEvent.UnWatch orig) -> {
                final ChunkTrackingEvent.Stop event = new ChunkTrackingEvent.Stop(orig.getLevel(), orig.getPlayer(), orig.getPos());
                events.fireEventHandlers(priority, event);
            });
        });

        events.registerEvent(TossItemEvent.class, priority -> {
            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (ItemTossEvent orig) -> {
                final TossItemEvent event = new TossItemEvent(orig.getPlayer(), orig.getEntity().getItem());
                events.fireEventHandlers(priority, event);
                if (event.isCanceled()) {
                    orig.setCanceled(true);
                }
            });
        });

        events.registerEvent(PlayerAttackEvent.class, priority -> {
            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (AttackEntityEvent orig) -> {
                final PlayerAttackEvent event = new PlayerAttackEvent(orig.getEntity(), orig.getTarget());
                events.fireEventHandlers(priority, event);
                if (event.isCanceled()) {
                    orig.setCanceled(true);
                }
            });
        });

        events.registerEvent(LivingHealEvent.class, priority -> {
            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (net.neoforged.neoforge.event.entity.living.LivingHealEvent orig) -> {
                final LivingHealEvent event = new LivingHealEvent(orig.getEntity(), orig.getAmount());
                events.fireEventHandlers(priority, event);
                if (event.isCanceled()) {
                    orig.setCanceled(true);
                }
            });
        });

        events.registerEvent(DigSpeedEvent.class, priority -> {
            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (PlayerEvent.BreakSpeed orig) -> {
                final DigSpeedEvent event = new DigSpeedEvent(orig.getEntity(), orig.getState(), orig.getOriginalSpeed());
                events.fireEventHandlers(priority, event);
                if (event.getSpeedOverride() != null) {
                    orig.setNewSpeed(event.getSpeedOverride());
                }
                if (event.isCanceled()) {
                    orig.setCanceled(true);
                }
            });
        });

        events.registerEvent(PlayerChangedDimensionEvent.class, priority -> {
            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (PlayerEvent.PlayerChangedDimensionEvent orig) -> {
                final PlayerChangedDimensionEvent event = new PlayerChangedDimensionEvent((ServerPlayer) orig.getEntity(), orig.getFrom(), orig.getTo());
                events.fireEventHandlers(priority, event);
            });
        });

        events.registerEvent(ItemCraftedEvent.class, priority -> {
            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (PlayerEvent.ItemCraftedEvent orig) -> {
                final ItemCraftedEvent event = new ItemCraftedEvent(orig.getEntity(), orig.getCrafting(), orig.getInventory());
                events.fireEventHandlers(priority, event);
            });
        });

        events.registerEvent(CommandEvent.class, priority -> {
            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (net.neoforged.neoforge.event.CommandEvent orig) -> {
                final CommandEvent event = new CommandEvent(orig.getParseResults());
                events.fireEventHandlers(priority, event);
                if (event.isCanceled()) {
                    orig.setCanceled(true);
                }
            });
        });
    }

}
