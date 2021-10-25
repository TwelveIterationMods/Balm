package net.blay09.mods.balm.forge.entity;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.entity.BalmEntities;
import net.blay09.mods.balm.forge.DeferredRegisters;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class ForgeBalmEntities implements BalmEntities {

    private static class Registrations {
        public final Map<EntityType<?>, AttributeSupplier> attributeSuppliers = new HashMap<>();

        @SubscribeEvent
        @SuppressWarnings("unchecked")
        public void registerAttributes(EntityAttributeCreationEvent event) {
            for (Map.Entry<EntityType<?>, AttributeSupplier> entry : attributeSuppliers.entrySet()) {
                event.put((EntityType<? extends LivingEntity>) entry.getKey(), entry.getValue());
            }
        }
    }

    private final Map<String, Registrations> registrations = new ConcurrentHashMap<>();

    @Override
    public <T extends Entity> DeferredObject<EntityType<T>> registerEntity(ResourceLocation identifier, EntityType.Builder<T> typeBuilder) {
        DeferredRegister<EntityType<?>> register = DeferredRegisters.get(ForgeRegistries.ENTITIES, identifier.getNamespace());
        RegistryObject<EntityType<T>> registryObject = register.register(identifier.getPath(), () -> typeBuilder.build(identifier.toString()));
        return new DeferredObject<>(identifier, registryObject, registryObject::isPresent);
    }

    @Override
    public <T extends LivingEntity> DeferredObject<EntityType<T>> registerEntity(ResourceLocation identifier, EntityType.Builder<T> typeBuilder, Supplier<AttributeSupplier.Builder> attributeBuilder) {
        final DeferredRegister<EntityType<?>> register = DeferredRegisters.get(ForgeRegistries.ENTITIES, identifier.getNamespace());
        final RegistryObject<EntityType<T>> registryObject = register.register(identifier.getPath(), () -> typeBuilder.build(identifier.toString()));
        final Registrations registrations = getActiveRegistrations();
        return new DeferredObject<>(identifier, () -> {
            EntityType<T> entityType = registryObject.get();
            registrations.attributeSuppliers.put(entityType, attributeBuilder.get().build());
            return entityType;
        }, registryObject::isPresent);
    }

    public void register() {
        FMLJavaModLoadingContext.get().getModEventBus().register(getActiveRegistrations());
    }

    private Registrations getActiveRegistrations() {
        return registrations.computeIfAbsent(ModLoadingContext.get().getActiveNamespace(), it -> new Registrations());
    }
}
