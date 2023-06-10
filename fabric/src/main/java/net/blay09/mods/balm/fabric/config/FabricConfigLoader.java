package net.blay09.mods.balm.fabric.config;

import net.blay09.mods.balm.api.config.BalmConfigData;
import net.blay09.mods.balm.api.config.ExpectedType;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class FabricConfigLoader {
    public static void load(File configFile, BalmConfigData configData) throws IOException {
        Map<String, Map<String, Object>> categories = Collections.emptyMap();
        if (configFile.exists()) {
            try (FileReader reader = new FileReader(configFile)) {
                categories = NotomlParser.parseProperties(reader);
            }
        }
        for (Map.Entry<String, Map<String, Object>> categoryEntry : categories.entrySet()) {
            var category = categoryEntry.getKey();
            var properties = categoryEntry.getValue();
            try {
                var categoryField = configData.getClass().getField(category);
                var categoryInstance = categoryField.get(configData);
                for (Map.Entry<String, Object> propertyEntry : properties.entrySet()) {
                    var property = propertyEntry.getKey();
                    var value = propertyEntry.getValue();
                    var propertyField = categoryInstance.getClass().getField(property);
                    var expectedTypeAnnotation = propertyField.getAnnotation(ExpectedType.class);
                    Object convertedValue = convertValue(value,
                            propertyField.getType(),
                            expectedTypeAnnotation != null ? expectedTypeAnnotation.value() : null);
                    if (convertedValue != null) {
                        propertyField.set(categoryInstance, convertedValue);
                    }
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static Object convertValue(Object value, Class<?> type, Class<?> innerType) {
        if (type == Integer.class) {
            if (value instanceof Number) {
                return ((Number) value).intValue();
            } else if (value instanceof String) {
                try {
                    return Integer.parseInt((String) value);
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        } else if (type == Double.class) {
            if (value instanceof Number) {
                return ((Number) value).doubleValue();
            } else if (value instanceof String) {
                try {
                    return Double.parseDouble((String) value);
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        } else if (type == Float.class) {
            if (value instanceof Number) {
                return ((Number) value).floatValue();
            } else if (value instanceof String) {
                try {
                    return Float.parseFloat((String) value);
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        } else if (type == Boolean.class) {
            if (value instanceof Number) {
                return ((Number) value).intValue() != 0;
            } else if (value instanceof String) {
                return Boolean.parseBoolean((String) value);
            } else if (value instanceof Boolean) {
                return value;
            }
        } else if (type == List.class) {
            if (value instanceof List<?>) {
                List<?> list = (List<?>) value;
                List<Object> convertedList = new ArrayList<>();
                for (Object entry : list) {
                    Object convertedEntry = convertValue(entry, innerType, null);
                    if (convertedEntry != null) {
                        convertedList.add(convertedEntry);
                    }
                }
                return convertedList;
            } else if (value.getClass() == innerType) {
                List<Object> list = new ArrayList<>();
                list.add(value);
                return list;
            } else {
                return null;
            }
        } else if (Enum.class.isAssignableFrom(type)) {
            if (value instanceof String) {
                try {
                    return parseEnumValue(type, (String) value);
                } catch (IllegalArgumentException e) {
                    return null;
                }
            } else {
                return null;
            }
        }
        return null;
    }

    private static Object parseEnumValue(Class<?> type, String value) {
        for (Object enumConstant : type.getEnumConstants()) {
            if (enumConstant.toString().equalsIgnoreCase(value)) {
                return enumConstant;
            }
        }

        return null;
    }

}