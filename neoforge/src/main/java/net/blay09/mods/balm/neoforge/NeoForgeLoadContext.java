package net.blay09.mods.balm.neoforge;

import net.blay09.mods.balm.api.BalmRuntimeLoadContext;
import net.neoforged.bus.api.IEventBus;

public record NeoForgeLoadContext(IEventBus modBus) implements BalmRuntimeLoadContext {
}
