package net.blay09.mods.forbic.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

public class ForbicKeyBindings {

    public static KeyMapping registerKeyBinding(String name, int keyCode, String category) {
        return KeyBindingHelper.registerKeyBinding(new KeyMapping(name, InputConstants.Type.KEYSYM, keyCode, category));
    }

    public static KeyMapping registerKeyBinding(String name, InputConstants.Type type, int keyCode, String category) {
        return KeyBindingHelper.registerKeyBinding(new KeyMapping(name, type, keyCode, category));
    }

    public static KeyMapping registerKeyBinding(String name, KeyConflictContext conflictContext, KeyModifier modifier, int keyCode, String category) {
        return KeyBindingHelper.registerKeyBinding(new KeyMapping(name, InputConstants.Type.KEYSYM, keyCode, category));
    }

    public static KeyMapping registerKeyBinding(String name, KeyConflictContext conflictContext, KeyModifier modifier, InputConstants.Type type, int keyCode, String category) {
        return KeyBindingHelper.registerKeyBinding(new KeyMapping(name, type, keyCode, category));
    }

    public static boolean isActiveAndMatches(KeyMapping keyMapping, int keyCode, int scanCode) {
        return keyMapping.matches(keyCode, scanCode);
    }

    public static boolean isActiveAndWasPressed(KeyMapping keyMapping) {
        return keyMapping.consumeClick();
    }

    /*private static boolean isActiveAndMatches(KeyMapping keyBinding, InputMappings.Input input) { TODO from crafting tweaks
        if (keyBinding.getKeyModifier() == KeyModifier.NONE) {
            if (KeyModifier.SHIFT.isActive(keyBinding.getKeyConflictContext()) || KeyModifier.CONTROL.isActive(keyBinding.getKeyConflictContext()) || KeyModifier.ALT.isActive(keyBinding.getKeyConflictContext())) {
                return false;
            }
        }

        return keyBinding.isActiveAndMatches(input);
    }

    public static boolean isActiveIgnoreContext(KeyBinding keyBinding) {
        return keyBinding.getKey().getType() == InputMappings.Type.KEYSYM && InputMappings.isKeyDown(Minecraft.getInstance().getMainWindow().getHandle(), keyBinding.getKey().getKeyCode());
    }*/

}
