package net.blay09.mods.balm.api.client.keymappings;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;

public interface BalmKeyMappings {
    KeyMapping registerKeyMapping(String name, int keyCode, String category);

    KeyMapping registerKeyMapping(String name, InputConstants.Type type, int keyCode, String category);

    KeyMapping registerKeyMapping(String name, KeyConflictContext conflictContext, KeyModifier modifier, int keyCode, String category);

    KeyMapping registerKeyMapping(String name, KeyConflictContext conflictContext, KeyModifier modifier, InputConstants.Type type, int keyCode, String category);

    boolean isActiveAndMatches(KeyMapping keyMapping, int keyCode, int scanCode);

    boolean isActiveAndMatches(KeyMapping keyMapping, InputConstants.Type type, int keyCode, int scanCode);

    boolean isActiveAndWasPressed(KeyMapping keyMapping);

    boolean isKeyDownIgnoreContext(KeyMapping keyMapping);

    boolean isActiveAndKeyDown(KeyMapping keyMapping);
}
