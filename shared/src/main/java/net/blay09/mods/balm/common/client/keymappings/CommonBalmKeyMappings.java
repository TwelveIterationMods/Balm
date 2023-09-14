package net.blay09.mods.balm.common.client.keymappings;

import com.mojang.blaze3d.platform.InputConstants;
import net.blay09.mods.balm.api.client.keymappings.BalmKeyMappings;
import net.blay09.mods.balm.api.client.keymappings.KeyConflictContext;
import net.blay09.mods.balm.api.client.keymappings.KeyModifier;
import net.blay09.mods.balm.api.client.keymappings.KeyModifiers;
import net.blay09.mods.balm.mixin.KeyMappingAccessor;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class CommonBalmKeyMappings implements BalmKeyMappings {
    private final Map<KeyMapping, Set<KeyMapping>> multiModifierKeyMappings = new ConcurrentHashMap<>();

    @Override
    public KeyMapping registerKeyMapping(String name, int keyCode, String category) {
        return registerKeyMapping(name, KeyConflictContext.UNIVERSAL, KeyModifier.NONE, InputConstants.Type.KEYSYM, keyCode, category);
    }

    @Override
    public KeyMapping registerKeyMapping(String name, InputConstants.Type type, int keyCode, String category) {
        return registerKeyMapping(name, KeyConflictContext.UNIVERSAL, KeyModifier.NONE, type, keyCode, category);
    }

    @Override
    public KeyMapping registerKeyMapping(String name, KeyConflictContext conflictContext, KeyModifier modifier, int keyCode, String category) {
        return registerKeyMapping(name, conflictContext, modifier, InputConstants.Type.KEYSYM, keyCode, category);
    }

    @Override
    public KeyMapping registerKeyMapping(String name, KeyConflictContext conflictContext, KeyModifiers modifiers, int keyCode, String category) {
        return registerKeyMapping(name, conflictContext, modifiers, InputConstants.Type.KEYSYM, keyCode, category);
    }

    protected void registerModifierKeyMappings(KeyMapping baseMapping, KeyConflictContext conflictContext, List<KeyModifier> keyModifiers) {
        for (int i = 0; i < keyModifiers.size(); i++) {
            String subName = i > 0 ? baseMapping.getName() + "_modifier_" + i : baseMapping.getName() + "_modifier";
            KeyMapping subKeyMapping = registerKeyMapping(subName,
                    conflictContext,
                    KeyModifier.NONE,
                    InputConstants.Type.KEYSYM,
                    toKeyCode(keyModifiers.get(i)),
                    baseMapping.getCategory());
            multiModifierKeyMappings.computeIfAbsent(baseMapping, it -> new HashSet<>()).add(subKeyMapping);
        }
    }

    protected void registerCustomModifierKeyMappings(KeyMapping baseMapping, KeyConflictContext conflictContext, List<InputConstants.Key> keyModifiers) {
        for (int i = 0; i < keyModifiers.size(); i++) {
            String subName = i > 0 ? baseMapping.getName() + "_modifier_" + i : baseMapping.getName() + "_modifier";
            KeyMapping subKeyMapping = registerKeyMapping(subName,
                    conflictContext,
                    KeyModifier.NONE,
                    InputConstants.Type.KEYSYM,
                    keyModifiers.get(i).getValue(),
                    baseMapping.getCategory());
            multiModifierKeyMappings.computeIfAbsent(baseMapping, it -> new HashSet<>()).add(subKeyMapping);
        }
    }

    private int toKeyCode(KeyModifier keyModifier) {
        return switch (keyModifier) {
            case SHIFT -> InputConstants.KEY_LSHIFT;
            case CONTROL -> InputConstants.KEY_LCONTROL;
            case ALT -> InputConstants.KEY_LALT;
            default -> -1;
        };
    }

    protected boolean areMultiModifiersActive(KeyMapping keyMapping) {
        Set<KeyMapping> modifierMappings = multiModifierKeyMappings.getOrDefault(keyMapping, Collections.emptySet());
        for (KeyMapping modifierMapping : modifierMappings) {
            if (modifierMapping.matches(InputConstants.KEY_LSHIFT, 0) || modifierMapping.matches(InputConstants.KEY_RSHIFT, 0)) {
                if (Screen.hasShiftDown()) {
                    continue;
                }
            }
            if (modifierMapping.matches(InputConstants.KEY_LCONTROL, 0) || modifierMapping.matches(InputConstants.KEY_RCONTROL, 0)) {
                if (Screen.hasControlDown()) {
                    continue;
                }
            }
            if (modifierMapping.matches(InputConstants.KEY_LALT, 0) || modifierMapping.matches(InputConstants.KEY_RALT, 0)) {
                if (Screen.hasAltDown()) {
                    continue;
                }
            }

            if (!isActiveAndKeyDown(modifierMapping)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isActiveAndKeyDown(@Nullable KeyMapping keyMapping) {
        if (!isActive(keyMapping)) {
            return false;
        }

        InputConstants.Key key = ((KeyMappingAccessor) keyMapping).getKey();
        return keyMapping.isDown() || (key.getValue() != -1 && key.getType() == InputConstants.Type.KEYSYM && InputConstants.isKeyDown(Minecraft.getInstance()
                .getWindow()
                .getWindow(), key.getValue()));
    }

    @Override
    public boolean isKeyDownIgnoreContext(@Nullable KeyMapping keyMapping) {
        if (!isActiveIgnoreContext(keyMapping)) {
            return false;
        }

        InputConstants.Key key = ((KeyMappingAccessor) keyMapping).getKey();
        return keyMapping.isDown() || (key.getValue() != -1 && key.getType() == InputConstants.Type.KEYSYM && InputConstants.isKeyDown(Minecraft.getInstance()
                .getWindow()
                .getWindow(), key.getValue()));
    }

    @Override
    public boolean isActiveAndWasPressed(@Nullable KeyMapping keyMapping) {
        return isActive(keyMapping) && keyMapping.consumeClick();
    }

    @Contract("null -> false")
    protected boolean isActive(@Nullable KeyMapping keyMapping) {
        if (keyMapping == null) {
            return false;
        }

        return isContextActive(keyMapping) && areMultiModifiersActive(keyMapping);
    }

    @Contract("null -> false")
    protected boolean isActiveIgnoreContext(@Nullable KeyMapping keyMapping) {
        if (keyMapping == null) {
            return false;
        }

        return areMultiModifiersActive(keyMapping);
    }

    protected abstract boolean isContextActive(KeyMapping keyMapping);
}
