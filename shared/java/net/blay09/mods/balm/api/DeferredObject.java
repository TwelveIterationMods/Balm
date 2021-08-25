package net.blay09.mods.balm.api;

import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class DeferredObject<T> {
    private final ResourceLocation identifier;
    private final Supplier<T> supplier;
    private T object;

    public DeferredObject(ResourceLocation identifier, Supplier<T> supplier) {
        this.identifier = identifier;
        this.supplier = supplier;
    }

    public T resolve() {
        if (object == null) {
            object = supplier.get();
        }
        return object;
    }

    public T get() {
        if (object == null) {
            throw new IllegalStateException("Tried to access deferred object before it was resolved.");
        }

        return object;
    }

    public DeferredObject<T> resolveImmediately() {
        resolve();
        return this;
    }

    public ResourceLocation getIdentifier() {
        return identifier;
    }
}
