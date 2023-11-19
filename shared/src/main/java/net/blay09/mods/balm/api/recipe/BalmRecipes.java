package net.blay09.mods.balm.api.recipe;

import net.blay09.mods.balm.api.DeferredObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.function.Supplier;

public interface BalmRecipes {
    <T extends Recipe<?>> DeferredObject<RecipeType<T>> registerRecipeType(Supplier<RecipeType<T>> supplier, ResourceLocation identifier);
}
