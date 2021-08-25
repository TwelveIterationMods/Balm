package net.blay09.mods.balm.api.client;

import net.blay09.mods.balm.api.client.rendering.BalmRenderers;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

public class BalmClient {

    private static BalmClientRuntime runtime;

    public static void __setupRuntime(BalmClientRuntime runtime) {
        BalmClient.runtime = runtime;
    }

    public static Player getClientPlayer() {
        return Minecraft.getInstance().player;
    }

    public static BalmRenderers getRenderers() {
        return runtime.getRenderers();
    }
}
