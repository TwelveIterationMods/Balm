package net.blay09.mods.balm.forge.client.keymappings;

import com.mojang.blaze3d.platform.InputConstants;
import net.blay09.mods.balm.api.client.keymappings.BalmKeyMappings;
import net.blay09.mods.balm.api.client.keymappings.KeyConflictContext;
import net.blay09.mods.balm.api.client.keymappings.KeyModifier;
import net.blay09.mods.balm.mixin.KeyMappingAccessor;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.settings.IKeyConflictContext;
import org.jetbrains.annotations.Nullable;

public class ForgeBalmKeyMappings implements BalmKeyMappings {

    @Override
    public KeyMapping registerKeyMapping(String name, int keyCode, String category) {
        KeyMapping keyMapping = new KeyMapping(name, InputConstants.Type.KEYSYM, keyCode, category);
        ClientRegistry.registerKeyBinding(keyMapping);
        return keyMapping;
    }

    @Override
    public KeyMapping registerKeyMapping(String name, InputConstants.Type type, int keyCode, String category) {
        KeyMapping keyMapping = new KeyMapping(name, type, keyCode, category);
        ClientRegistry.registerKeyBinding(keyMapping);
        return keyMapping;
    }

    @Override
    public KeyMapping registerKeyMapping(String name, KeyConflictContext conflictContext, KeyModifier modifier, int keyCode, String category) {
        KeyMapping keyMapping = new KeyMapping(name, toForge(conflictContext), toForge(modifier), InputConstants.Type.KEYSYM, keyCode, category);
        ClientRegistry.registerKeyBinding(keyMapping);
        return keyMapping;
    }

    @Override
    public KeyMapping registerKeyMapping(String name, KeyConflictContext conflictContext, KeyModifier modifier, InputConstants.Type type, int keyCode, String category) {
        KeyMapping keyMapping = new KeyMapping(name, toForge(conflictContext), toForge(modifier), type, keyCode, category);
        ClientRegistry.registerKeyBinding(keyMapping);
        return keyMapping;
    }

    @Override
    public boolean isActiveAndMatches(@Nullable KeyMapping keyMapping, InputConstants.Key input) {
        return keyMapping != null && keyMapping.isActiveAndMatches(input);
    }

    @Override
    public boolean isActiveAndMatches(@Nullable KeyMapping keyMapping, int keyCode, int scanCode) {
        return keyMapping != null && keyMapping.isActiveAndMatches(InputConstants.getKey(keyCode, scanCode));
    }

    @Override
    public boolean isActiveAndMatches(@Nullable KeyMapping keyMapping, InputConstants.Type type, int keyCode, int scanCode) {
        if(keyMapping == null) {
            return false;
        }

        return type == InputConstants.Type.MOUSE ? keyMapping.isActiveAndMatches(InputConstants.Type.MOUSE.getOrCreate(keyCode)) : keyMapping.isActiveAndMatches(InputConstants.getKey(keyCode, scanCode));
    }

    @Override
    public boolean isActiveAndWasPressed(@Nullable KeyMapping keyMapping) {
        return keyMapping != null && keyMapping.consumeClick();
    }

    private boolean isActiveAndMatchesStrictModifier(@Nullable KeyMapping keyMapping, int keyCode, int scanCode) {
        if(keyMapping == null) {
            return false;
        }

        if (keyMapping.getKeyModifier() == net.minecraftforge.client.settings.KeyModifier.NONE) {
            if (net.minecraftforge.client.settings.KeyModifier.SHIFT.isActive(keyMapping.getKeyConflictContext())
                    || net.minecraftforge.client.settings.KeyModifier.CONTROL.isActive(keyMapping.getKeyConflictContext())
                    || net.minecraftforge.client.settings.KeyModifier.ALT.isActive(keyMapping.getKeyConflictContext())) {
                return false;
            }
        }

        return keyMapping.matches(keyCode, scanCode);
    }

    @Override
    public boolean isKeyDownIgnoreContext(@Nullable KeyMapping keyMapping) {
        if(keyMapping == null) {
            return false;
        }

        InputConstants.Key key = ((KeyMappingAccessor) keyMapping).getKey();
        return keyMapping.isDown() || (key.getValue() != -1 && key.getType() == InputConstants.Type.KEYSYM && InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), key.getValue()));
    }

    @Override
    public boolean isActiveAndKeyDown(@Nullable KeyMapping keyMapping) {
        if(keyMapping == null) {
            return false;
        }

        InputConstants.Key key = ((KeyMappingAccessor) keyMapping).getKey();
        return keyMapping.isDown() || (key.getValue() != -1 && key.getType() == InputConstants.Type.KEYSYM && InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), key.getValue()));
    }

    private static IKeyConflictContext toForge(KeyConflictContext context) {
        return switch (context) {
            case UNIVERSAL -> net.minecraftforge.client.settings.KeyConflictContext.UNIVERSAL;
            case GUI -> net.minecraftforge.client.settings.KeyConflictContext.GUI;
            case INGAME -> net.minecraftforge.client.settings.KeyConflictContext.IN_GAME;
        };
    }

    private static net.minecraftforge.client.settings.KeyModifier toForge(KeyModifier modifier) {
        return switch (modifier) {
            case SHIFT -> net.minecraftforge.client.settings.KeyModifier.SHIFT;
            case CONTROL -> net.minecraftforge.client.settings.KeyModifier.CONTROL;
            case ALT -> net.minecraftforge.client.settings.KeyModifier.ALT;
            default -> net.minecraftforge.client.settings.KeyModifier.NONE;
        };
    }

}
