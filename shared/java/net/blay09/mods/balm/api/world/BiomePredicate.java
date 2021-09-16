package net.blay09.mods.balm.api.world;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

@FunctionalInterface
public interface BiomePredicate {
    boolean test(ResourceLocation name, Biome.BiomeCategory category, Biome.Precipitation precipitation, float temperature, float downfall);
}
