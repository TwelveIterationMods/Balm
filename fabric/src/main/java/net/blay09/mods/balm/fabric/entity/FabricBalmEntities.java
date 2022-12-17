package net.blay09.mods.balm.fabric.entity;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.entity.BalmEntities;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

import java.util.function.Supplier;

public class FabricBalmEntities implements BalmEntities {

    @Override
    public <T extends Entity> DeferredObject<EntityType<T>> registerEntity(ResourceLocation identifier, EntityType.Builder<T> typeBuilder) {
        return new DeferredObject<>(identifier, () -> {
            EntityType<T> entityType = typeBuilder.build(identifier.toString());
            return Registry.register(BuiltInRegistries.ENTITY_TYPE, identifier, entityType);
        }).resolveImmediately();
    }

    @Override
    public <T extends LivingEntity> DeferredObject<EntityType<T>> registerEntity(ResourceLocation identifier, EntityType.Builder<T> typeBuilder, Supplier<AttributeSupplier.Builder> attributeBuilder) {
        return new DeferredObject<>(identifier, () -> {
            EntityType<T> entityType = typeBuilder.build(identifier.toString());
            FabricDefaultAttributeRegistry.register(entityType, attributeBuilder.get());
            return Registry.register(BuiltInRegistries.ENTITY_TYPE, identifier, entityType);
        }).resolveImmediately();
    }
}
