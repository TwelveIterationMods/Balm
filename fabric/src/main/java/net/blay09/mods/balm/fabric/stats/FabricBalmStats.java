package net.blay09.mods.balm.fabric.stats;

import net.blay09.mods.balm.api.stats.BalmStats;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;

public class FabricBalmStats implements BalmStats {
    @Override
    public void registerCustomStat(ResourceLocation identifier) {
        Registry.register(BuiltInRegistries.CUSTOM_STAT, identifier.getPath(), identifier);
        Stats.CUSTOM.get(identifier, StatFormatter.DEFAULT);
    }
}
