package net.blay09.mods.balm.forge;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.Collection;

public class DeferredRegisters {
    private static final Table<IForgeRegistry<?>, String, DeferredRegister<?>> deferredRegisters = HashBasedTable.create();

    @SuppressWarnings("unchecked")
    public static <T extends IForgeRegistryEntry<T>> DeferredRegister<T> get(IForgeRegistry<T> registry, String modId) {
        DeferredRegister<?> register = deferredRegisters.get(registry, modId);
        if (register == null) {
            register = DeferredRegister.create(registry, modId);
            deferredRegisters.put(registry, modId, register);
        }

        return (DeferredRegister<T>) register;
    }

    public static Collection<DeferredRegister<?>> getByModId(String modId) {
        return deferredRegisters.column(modId).values();
    }
}
