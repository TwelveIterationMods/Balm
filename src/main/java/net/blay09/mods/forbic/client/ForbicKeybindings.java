package net.blay09.mods.forbic.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;

public class ForbicKeybindings {
    public static void registerKeyBinding(String name, InputConstants.Type type, int keyCode, String category) {
        KeyBindingHelper.registerKeyBinding(new KeyMapping(name, type, keyCode, category));
    }
}
