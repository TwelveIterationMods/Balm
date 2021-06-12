package net.blay09.mods.forbic;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

public class ForbicClient {
    public static Player getClientPlayer() {
        return Minecraft.getInstance().player;
    }
}
