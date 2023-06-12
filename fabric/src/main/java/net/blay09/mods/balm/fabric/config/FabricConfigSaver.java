package net.blay09.mods.balm.fabric.config;

import com.mojang.logging.LogUtils;
import net.blay09.mods.balm.api.config.BalmConfigData;
import net.blay09.mods.balm.api.config.Comment;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FabricConfigSaver {

    private static final Logger logger = LogUtils.getLogger();

    public static void save(File configFile, BalmConfigData configData) throws IOException {
        var categories = new HashMap<String, Map<String, Object>>();
        var comments = new HashMap<String, String>();
        var rootProperties = new HashMap<String, Object>();
        for (Field rootField : configData.getClass().getFields()) {
            var isCategory = !isPropertyType(rootField.getType());
            var category = isCategory ? rootField.getName() : "";
            if (isCategory) {
                var categoryComment = rootField.getAnnotation(Comment.class);
                if (categoryComment != null) {
                    comments.put(category, categoryComment.value());
                }
                var properties = new HashMap<String, Object>();
                try {
                    var categoryInstance = rootField.get(configData);
                    for (Field propertyField : categoryInstance.getClass().getFields()) {
                        var property = propertyField.getName();
                        var propertyComment = propertyField.getAnnotation(Comment.class);
                        if (propertyComment != null) {
                            comments.put(category + "." + property, propertyComment.value());
                        }
                        try {
                            var value = propertyField.get(categoryInstance);
                            properties.put(property, value);
                        } catch (Exception e) {
                            logger.error("Failed to save config category {}", category, e);
                        }
                    }
                } catch (Exception e) {
                    logger.error("Failed to save config category {}", category, e);
                }
                categories.put(category, properties);
            } else {
                try {
                    var property = rootField.getName();
                    var propertyComment = rootField.getAnnotation(Comment.class);
                    if (propertyComment != null) {
                        comments.put(category + "." + property, propertyComment.value());
                    }
                    var value = rootField.get(configData);
                    rootProperties.put(property, value);
                } catch (Exception e) {
                    logger.error("Failed to save config property {}", rootField.getName(), e);
                }
            }
        }
        if (!rootProperties.isEmpty()) {
            categories.put("", rootProperties);
        }
        try (FileWriter writer = new FileWriter(configFile)) {
            NotomlSerializer.serialize(writer, categories, comments);
        }
    }

    private static boolean isPropertyType(Class<?> type) {
        return type.isPrimitive()
                || type == String.class
                || type == Integer.class
                || type == Boolean.class
                || type == Float.class
                || type == Double.class
                || type == List.class
                || Enum.class.isAssignableFrom(type);
    }
}
