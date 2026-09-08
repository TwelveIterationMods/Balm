package net.blay09.mods.balm.neoforge.world.item.crafting.internal;

import net.blay09.mods.balm.core.BalmRegistrar;
import net.blay09.mods.balm.world.item.crafting.BalmRecipeTypeRegistration;
import net.blay09.mods.balm.world.item.crafting.internal.BalmRecipeTypeRegistrarImpl;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class NeoForgeBalmRecipeTypeRegistrar extends BalmRecipeTypeRegistrarImpl {

    private final List<Holder<? extends RecipeType<?>>> recipeTypes = new ArrayList<>();

    public NeoForgeBalmRecipeTypeRegistrar(BalmRegistrar registrar, String namespace) {
        super(registrar, namespace);
        NeoForge.EVENT_BUS.addListener(OnDatapackSyncEvent.class, this::onDatapackSync);
    }

    @Override
    public <TRecipeInput extends RecipeInput, TRecipe extends Recipe<TRecipeInput>> BalmRecipeTypeRegistration<TRecipeInput, TRecipe> register(String name, Function<Identifier, ? extends RecipeType<TRecipe>> constructor) {
        final var registration = super.register(name, constructor);
        recipeTypes.add(registration.asHolder());
        return registration;
    }

    private void onDatapackSync(OnDatapackSyncEvent event) {
        for (final var recipeType : recipeTypes) {
            event.sendRecipes(recipeType.value());
        }
    }
}
