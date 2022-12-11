package net.blay09.mods.balm.fabric.client.keymappings;

import com.mojang.blaze3d.platform.InputConstants;
import net.blay09.mods.balm.api.client.keymappings.BalmKeyMappings;
import net.blay09.mods.balm.api.client.keymappings.KeyConflictContext;
import net.blay09.mods.balm.api.client.keymappings.KeyModifier;
import net.blay09.mods.balm.mixin.KeyMappingAccessor;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.Nullable;

public class FabricBalmKeyMappings implements BalmKeyMappings {

    @Override
    public KeyMapping registerKeyMapping(String name, int keyCode, String category) {
        return KeyBindingHelper.registerKeyBinding(new KeyMapping(name, InputConstants.Type.KEYSYM, keyCode, category));
    }

    @Override
    public KeyMapping registerKeyMapping(String name, InputConstants.Type type, int keyCode, String category) {
        return KeyBindingHelper.registerKeyBinding(new KeyMapping(name, type, keyCode, category));
    }

    @Override
    public KeyMapping registerKeyMapping(String name, KeyConflictContext conflictContext, KeyModifier modifier, int keyCode, String category) {
        return KeyBindingHelper.registerKeyBinding(new KeyMapping(name, InputConstants.Type.KEYSYM, keyCode, category));
    }

    @Override
    public KeyMapping registerKeyMapping(String name, KeyConflictContext conflictContext, KeyModifier modifier, InputConstants.Type type, int keyCode, String category) {
        return KeyBindingHelper.registerKeyBinding(new KeyMapping(name, type, keyCode, category));
    }

    @Override
    public boolean isActiveAndMatches(@Nullable KeyMapping keyMapping, InputConstants.Key input) {
        if (keyMapping == null) {
            return false;
        }

        return input.getType() == InputConstants.Type.MOUSE
                ? keyMapping.matchesMouse(input.getValue())
                : keyMapping.matches(input.getType() == InputConstants.Type.KEYSYM ? input.getValue() : InputConstants.UNKNOWN.getValue(),
                input.getType() == InputConstants.Type.SCANCODE ? input.getValue() : InputConstants.UNKNOWN.getValue());
    }

    @Override
    public boolean isActiveAndMatches(@Nullable KeyMapping keyMapping, int keyCode, int scanCode) {
        return keyMapping != null && keyMapping.matches(keyCode, scanCode);
    }

    @Override
    public boolean isActiveAndMatches(@Nullable KeyMapping keyMapping, InputConstants.Type type, int keyCode, int scanCode) {
        if (keyMapping == null) {
            return false;
        }

        return type == InputConstants.Type.MOUSE ? keyMapping.matchesMouse(keyCode) : keyMapping.matches(keyCode, scanCode);
    }

    @Override
    public boolean isActiveAndWasPressed(@Nullable KeyMapping keyMapping) {
        return keyMapping != null && keyMapping.consumeClick();
    }

    private boolean isActiveAndMatchesStrictModifier(@Nullable KeyMapping keyMapping, int keyCode, int scanCode) {
        /* for forge: if (keyBinding.getKeyModifier() == KeyModifier.NONE) {
            if (KeyModifier.SHIFT.isActive(keyBinding.getKeyConflictContext()) || KeyModifier.CONTROL.isActive(keyBinding.getKeyConflictContext()) || KeyModifier.ALT.isActive(keyBinding.getKeyConflictContext())) {
                return false;
            }
        }*/

        return keyMapping != null && keyMapping.matches(keyCode, scanCode);
    }

    @Override
    public boolean isKeyDownIgnoreContext(@Nullable KeyMapping keyMapping) {
        if (keyMapping == null) {
            return false;
        }

        InputConstants.Key key = ((KeyMappingAccessor) keyMapping).getKey();
        return keyMapping.isDown() || (key.getValue() != -1 && key.getType() == InputConstants.Type.KEYSYM && InputConstants.isKeyDown(Minecraft.getInstance()
                .getWindow()
                .getWindow(), key.getValue()));
    }

    @Override
    public boolean isActiveAndKeyDown(@Nullable KeyMapping keyMapping) {
        if (keyMapping == null) {
            return false;
        }

        InputConstants.Key key = ((KeyMappingAccessor) keyMapping).getKey();
        return keyMapping.isDown() || (key.getValue() != -1 && key.getType() == InputConstants.Type.KEYSYM && InputConstants.isKeyDown(Minecraft.getInstance()
                .getWindow()
                .getWindow(), key.getValue()));
    }

}
