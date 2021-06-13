package net.blay09.mods.forbic.core;

import java.util.function.Supplier;

public class DeferredObject<T> {
    private final Supplier<T> supplier;
    private T object;

    public DeferredObject(Supplier<T> supplier) {
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
}
