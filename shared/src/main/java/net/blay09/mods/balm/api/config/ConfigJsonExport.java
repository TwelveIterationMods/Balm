package net.blay09.mods.balm.api.config;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * This is used internally to generate a JSON file containing all config properties and their metadata for use in the documentation website.
 */
public class ConfigJsonExport {

    record ConfigProperty(String name, String type, String description, Object defaultValue, @Nullable String[] validValues) {
    }

    public static ArrayList<ConfigProperty> export(Class<?> configDataClass) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        final var defaults = configDataClass.getConstructor().newInstance();
        final var properties = new ArrayList<ConfigProperty>();
        for (var field : configDataClass.getDeclaredFields()) {
            if (isProperty(field)) {
                final var name = field.getName();
                final var type = field.getType().getSimpleName();
                final var description = field.getAnnotation(Comment.class).value();
                final var defaultValue = field.get(defaults);
                final var validValues = getValidValues(field);
                properties.add(new ConfigProperty(name, type, description, defaultValue, validValues));
            } else {
                properties.addAll(export(field.getType()));
            }
        }
        return properties;
    }

    @Nullable
    private static String[] getValidValues(Field field) {
        if (field.getType().isEnum()) {
            final var enumConstants = field.getType().getEnumConstants();
            final var validValues = new String[enumConstants.length];
            for (int i = 0; i < enumConstants.length; i++) {
                validValues[i] = enumConstants[i].toString();
            }
            return validValues;
        }
        return null;
    }

    private static boolean isProperty(Field field) {
        final var type = field.getType();
        return type.isPrimitive() || type.isEnum() || type == String.class || type == List.class || type == Set.class || type == ResourceLocation.class;
    }
}
