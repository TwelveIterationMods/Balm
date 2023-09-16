package net.blay09.mods.balm.api.client.keymappings;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;

import java.util.Optional;

public interface BalmKeyMappings {
    KeyMapping registerKeyMapping(String name, int keyCode, String category);

    KeyMapping registerKeyMapping(String name, InputConstants.Type type, int keyCode, String category);

    KeyMapping registerKeyMapping(String name, KeyConflictContext conflictContext, KeyModifier modifier, int keyCode, String category);

    KeyMapping registerKeyMapping(String name, KeyConflictContext conflictContext, KeyModifiers modifiers, int keyCode, String category);

    KeyMapping registerKeyMapping(String name, KeyConflictContext conflictContext, KeyModifier modifier, InputConstants.Type type, int keyCode, String category);

    KeyMapping registerKeyMapping(String name, KeyConflictContext conflictContext, KeyModifiers modifiers, InputConstants.Type type, int keyCode, String category);

    default boolean isActiveAndMatches(KeyMapping keyMapping, int keyCode, int scanCode) {
        return isActiveAndMatches(keyMapping, InputConstants.getKey(keyCode, scanCode));
    }

    default boolean isActiveAndMatches(KeyMapping keyMapping, InputConstants.Type type, int keyCode, int scanCode) {
        return isActiveAndMatches(keyMapping, type.getOrCreate(type == InputConstants.Type.SCANCODE ? scanCode : keyCode));
    }

    boolean isActiveAndMatches(KeyMapping keyMapping, InputConstants.Key input);

    boolean isActiveAndWasPressed(KeyMapping keyMapping);

    boolean isKeyDownIgnoreContext(KeyMapping keyMapping);

    boolean isActiveAndKeyDown(KeyMapping keyMapping);

    Optional<Boolean> conflictsWith(KeyMapping first, KeyMapping second);

    void ignoreConflicts(KeyMapping keyMapping);

    boolean shouldIgnoreConflicts(KeyMapping keyMapping);
}
