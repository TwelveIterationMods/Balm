package net.blay09.mods.balm.neoforge.recipe;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.recipe.BalmRecipes;
import net.blay09.mods.balm.neoforge.DeferredRegisters;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.ForgeRegistries;
import net.neoforged.neoforge.registries.RegistryObject;

import java.util.function.Supplier;

public class NeoForgeBalmRecipes implements BalmRecipes {
    @Override
    public <T extends Recipe<?>> DeferredObject<RecipeType<T>> registerRecipeType(Supplier<RecipeType<T>> supplier, ResourceLocation identifier) {
        DeferredRegister<RecipeType<?>> register = DeferredRegisters.get(ForgeRegistries.RECIPE_TYPES, identifier.getNamespace());
        RegistryObject<RecipeType<T>> registryObject = register.register(identifier.getPath(), supplier);
        return new DeferredObject<>(identifier, registryObject, registryObject::isPresent);
    }

}
