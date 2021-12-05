package net.blay09.mods.balm.mixin;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.loot.BalmLootModifier;
import net.blay09.mods.balm.fabric.loot.FabricBalmLootTables;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(LootTable.class)
public class LootTableMixin {
    @Inject(method = "getRandomItems(Lnet/minecraft/world/level/storage/loot/LootContext;)Ljava/util/List;", at = @At("RETURN"), cancellable = true)
    public void getRandomItems(LootContext lootContext, CallbackInfoReturnable<List<ItemStack>> callbackInfo) {
        var drops = callbackInfo.getReturnValue();
        var lootModifiers = ((FabricBalmLootTables) Balm.getLootTables()).lootModifiers;
        for (BalmLootModifier modifier : lootModifiers.values()) {
            drops = modifier.apply(lootContext, drops);
        }

        callbackInfo.setReturnValue(drops);
    }
}
