package net.blay09.mods.balm.forge.recipe;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.recipe.BalmRecipes;
import net.blay09.mods.balm.forge.DeferredRegisters;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ForgeBalmRecipes implements BalmRecipes {
    @Override
    public <T extends Recipe<?>> DeferredObject<RecipeType<T>> registerRecipeType(Supplier<RecipeType<T>> supplier, ResourceLocation identifier) {
        DeferredRegister<RecipeType<?>> register = DeferredRegisters.get(ForgeRegistries.RECIPE_TYPES, identifier.getNamespace());
        RegistryObject<RecipeType<T>> registryObject = register.register(identifier.getPath(), supplier);
        return new DeferredObject<>(identifier, registryObject, registryObject::isPresent);
    }

}
