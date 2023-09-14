package net.blay09.mods.balm.forge.client.keymappings;

import com.mojang.blaze3d.platform.InputConstants;
import net.blay09.mods.balm.api.client.keymappings.BalmKeyMappings;
import net.blay09.mods.balm.api.client.keymappings.KeyConflictContext;
import net.blay09.mods.balm.api.client.keymappings.KeyModifier;
import net.blay09.mods.balm.api.client.keymappings.KeyModifiers;
import net.blay09.mods.balm.mixin.KeyMappingAccessor;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.IKeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.jetbrains.annotations.Nullable;

public class ForgeBalmKeyMappings implements BalmKeyMappings {
    private static class Registrations {
        public final List<KeyMapping> keyMappings = new ArrayList<>();

        @SubscribeEvent
        public void registerKeyMappings(RegisterKeyMappingsEvent event) {
            keyMappings.forEach(event::register);
        }
    }

    private final Map<String, Registrations> registrations = new ConcurrentHashMap<>();
    private final Map<KeyMapping, Set<KeyMapping>> multiModifierKeyMappings = new ConcurrentHashMap<>();

    @Override
    public KeyMapping registerKeyMapping(String name, int keyCode, String category) {
        KeyMapping keyMapping = new KeyMapping(name, InputConstants.Type.KEYSYM, keyCode, category);
        getActiveRegistrations().keyMappings.add(keyMapping);
        return keyMapping;
    }

    @Override
    public KeyMapping registerKeyMapping(String name, InputConstants.Type type, int keyCode, String category) {
        KeyMapping keyMapping = new KeyMapping(name, type, keyCode, category);
        getActiveRegistrations().keyMappings.add(keyMapping);
        return keyMapping;
    }

    @Override
    public KeyMapping registerKeyMapping(String name, KeyConflictContext conflictContext, KeyModifier modifier, int keyCode, String category) {
        KeyMapping keyMapping = new KeyMapping(name, toForge(conflictContext), toForge(modifier), InputConstants.Type.KEYSYM, keyCode, category);
        getActiveRegistrations().keyMappings.add(keyMapping);
        return keyMapping;
    }

    @Override
    public KeyMapping registerKeyMapping(String name, KeyConflictContext conflictContext, KeyModifier modifier, InputConstants.Type type, int keyCode, String category) {
        KeyMapping keyMapping = new KeyMapping(name, toForge(conflictContext), toForge(modifier), type, keyCode, category);
        getActiveRegistrations().keyMappings.add(keyMapping);
        return keyMapping;
    }

    @Override
    public KeyMapping registerKeyMapping(String name, KeyConflictContext conflictContext, KeyModifiers modifiers, int keyCode, String category) {
        return registerKeyMapping(name, conflictContext, modifiers, InputConstants.Type.KEYSYM, keyCode, category);
    }

    @Override
    public KeyMapping registerKeyMapping(String name, KeyConflictContext conflictContext, KeyModifiers modifiers, InputConstants.Type type, int keyCode, String category) {
        var keyModifiers = modifiers.asList();
        var mainModifier = !keyModifiers.isEmpty() ? keyModifiers.get(0) : KeyModifier.NONE;
        KeyMapping keyMapping = new KeyMapping(name, toForge(conflictContext), toForge(mainModifier), type, keyCode, category);
        getActiveRegistrations().keyMappings.add(keyMapping);
        if (keyModifiers.size() > 1) {
            registerModifierKeyMappings(keyMapping, conflictContext, keyModifiers.subList(1, keyModifiers.size()));
        }
        if (modifiers.hasCustomModifiers()) {
            registerCustomModifierKeyMappings(keyMapping, conflictContext, modifiers.getCustomModifiers());
        }
        return keyMapping;
    }

    private void registerModifierKeyMappings(KeyMapping baseMapping, KeyConflictContext conflictContext, List<KeyModifier> keyModifiers) {
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

    private void registerCustomModifierKeyMappings(KeyMapping baseMapping, KeyConflictContext conflictContext, List<InputConstants.Key> keyModifiers) {
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

    @Override
    public boolean isActiveAndMatches(@Nullable KeyMapping keyMapping, InputConstants.Key input) {
        if (keyMapping == null) {
            return false;
        }

        if (!areMultiModifiersActive(keyMapping)) {
            return false;
        }

        return keyMapping.isActiveAndMatches(input);
    }

    @Override
    public boolean isActiveAndMatches(@Nullable KeyMapping keyMapping, int keyCode, int scanCode) {
        if (keyMapping == null) {
            return false;
        }

        if (!areMultiModifiersActive(keyMapping)) {
            return false;
        }

        return keyMapping.isActiveAndMatches(InputConstants.getKey(keyCode, scanCode));
    }

    @Override
    public boolean isActiveAndMatches(@Nullable KeyMapping keyMapping, InputConstants.Type type, int keyCode, int scanCode) {
        if (keyMapping == null) {
            return false;
        }

        if (!areMultiModifiersActive(keyMapping)) {
            return false;
        }

        return type == InputConstants.Type.MOUSE ? keyMapping.isActiveAndMatches(InputConstants.Type.MOUSE.getOrCreate(keyCode)) : keyMapping.isActiveAndMatches(
                InputConstants.getKey(keyCode, scanCode));
    }

    @Override
    public boolean isActiveAndWasPressed(@Nullable KeyMapping keyMapping) {
        if (keyMapping == null) {
            return false;
        }

        if (!areMultiModifiersActive(keyMapping)) {
            return false;
        }

        return keyMapping.consumeClick();
    }

    private boolean isActiveAndMatchesStrictModifier(@Nullable KeyMapping keyMapping, int keyCode, int scanCode) {
        if (keyMapping == null) {
            return false;
        }

        if (!areMultiModifiersActive(keyMapping)) {
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
        if (keyMapping == null) {
            return false;
        }

        if (!areMultiModifiersActive(keyMapping)) {
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

        if (!areMultiModifiersActive(keyMapping)) {
            return false;
        }

        InputConstants.Key key = ((KeyMappingAccessor) keyMapping).getKey();
        return keyMapping.isDown() || (key.getValue() != -1 && key.getType() == InputConstants.Type.KEYSYM && InputConstants.isKeyDown(Minecraft.getInstance()
                .getWindow()
                .getWindow(), key.getValue()));
    }

    private boolean areMultiModifiersActive(KeyMapping keyMapping) {
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

    private static List<net.minecraftforge.client.settings.KeyModifier> toForge(KeyModifiers modifiers) {
        List<net.minecraftforge.client.settings.KeyModifier> forgeModifiers = new ArrayList<>();
        if (modifiers.contains(KeyModifier.SHIFT)) {
            forgeModifiers.add(net.minecraftforge.client.settings.KeyModifier.SHIFT);
        }
        if (modifiers.contains(KeyModifier.CONTROL)) {
            forgeModifiers.add(net.minecraftforge.client.settings.KeyModifier.CONTROL);
        }
        if (modifiers.contains(KeyModifier.ALT)) {
            forgeModifiers.add(net.minecraftforge.client.settings.KeyModifier.ALT);
        }
        return forgeModifiers;
    }

    public void register() {
        FMLJavaModLoadingContext.get().getModEventBus().register(getActiveRegistrations());
    }

    private Registrations getActiveRegistrations() {
        return registrations.computeIfAbsent(ModLoadingContext.get().getActiveNamespace(), it -> new Registrations());
    }

}
