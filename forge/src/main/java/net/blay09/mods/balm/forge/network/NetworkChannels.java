package net.blay09.mods.balm.forge.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NetworkChannels {
    private static final String version = "1.0"; // all mods will just have same version for now until I clean this up
    private static final Map<String, SimpleChannel> channels = new ConcurrentHashMap<>();

    public static SimpleChannel get(String modId) {
        return channels.computeIfAbsent(modId, key -> NetworkRegistry.newSimpleChannel(new ResourceLocation(key, "network"), () -> version, it -> it.equals(version), it -> it.equals(version)));
    }
}
