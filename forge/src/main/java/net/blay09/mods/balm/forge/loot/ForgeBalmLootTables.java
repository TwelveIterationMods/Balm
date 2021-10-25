package net.blay09.mods.balm.forge.loot;

import com.google.gson.JsonObject;
import net.blay09.mods.balm.api.loot.BalmLootModifier;
import net.blay09.mods.balm.api.loot.BalmLootTables;
import net.blay09.mods.balm.forge.DeferredRegisters;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class ForgeBalmLootTables implements BalmLootTables {
    @Override
    public void registerLootModifier(ResourceLocation identifier, BalmLootModifier modifier) {
        DeferredRegister<GlobalLootModifierSerializer<?>> register = DeferredRegisters.get(ForgeRegistries.LOOT_MODIFIER_SERIALIZERS, identifier.getNamespace());
        register.register(identifier.getPath(), () -> new GlobalLootModifierSerializer<>() {
            @Override
            public IGlobalLootModifier read(ResourceLocation location, JsonObject object, LootItemCondition[] conditions) {
                return new LootModifier(conditions) {
                    @Override
                    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
                        return modifier.apply(context, generatedLoot);
                    }
                };
            }

            @Override
            public JsonObject write(IGlobalLootModifier instance) {
                return new JsonObject();
            }
        });
    }
}
