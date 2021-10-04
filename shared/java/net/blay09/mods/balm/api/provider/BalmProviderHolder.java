package net.blay09.mods.balm.api.provider;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Direction;

import java.util.List;

public interface BalmProviderHolder {
    List<Object> getProviders();
    List<Pair<Direction, Object>> getSidedProviders();
}
