package net.blay09.mods.balm.fabric.world.item.crafting.internal;

import net.blay09.mods.balm.core.BalmRegistrar;
import net.blay09.mods.balm.world.item.crafting.BalmRecipeSerializerRegistration;
import net.blay09.mods.balm.world.item.crafting.internal.BalmRecipeTypeRegistrarImpl;
import net.fabricmc.fabric.api.recipe.v1.sync.RecipeSynchronization;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.function.Function;

public class FabricBalmRecipeTypeRegistrar extends BalmRecipeTypeRegistrarImpl {

    public FabricBalmRecipeTypeRegistrar(BalmRegistrar registrar, String namespace) {
        super(registrar, namespace);
    }

    @Override
    public <TRecipeInput extends RecipeInput, TRecipe extends Recipe<TRecipeInput>> BalmRecipeSerializerRegistration<TRecipe> registerSerializer(String name, Function<Identifier, RecipeSerializer<TRecipe>> constructor) {
        final var registration = super.registerSerializer(name, constructor);
        RecipeSynchronization.synchronizeRecipeSerializer(registration.asHolder().value());
        return registration;
    }
}
