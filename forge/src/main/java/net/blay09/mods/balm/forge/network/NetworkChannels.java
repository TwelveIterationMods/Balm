package net.blay09.mods.balm.forge.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fmllegacy.network.NetworkRegistry;
import net.minecraftforge.fmllegacy.network.simple.SimpleChannel;

import java.util.HashMap;
import java.util.Map;

public class NetworkChannels {
    private static final String version = "1.0"; // all mods will just have same version for now until I clean this up
    private static final Map<String, SimpleChannel> channels = new HashMap<>();

    public static SimpleChannel get(String modId) {
        return channels.computeIfAbsent(modId, key -> NetworkRegistry.newSimpleChannel(new ResourceLocation(key, "network"), () -> version, it -> it.equals(version), it -> it.equals(version)));
    }
}
