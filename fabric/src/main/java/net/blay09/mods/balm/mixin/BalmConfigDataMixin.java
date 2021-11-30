package net.blay09.mods.balm.mixin;

import me.shedaniel.autoconfig.ConfigData;
import net.blay09.mods.balm.api.config.BalmConfigData;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BalmConfigData.class)
public interface BalmConfigDataMixin extends ConfigData {
}
