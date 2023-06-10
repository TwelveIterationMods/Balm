package net.blay09.mods.balm.fabric.config;

import net.blay09.mods.balm.api.config.BalmConfigData;
import net.blay09.mods.balm.api.config.Comment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class FabricConfigSaver {
    public static void save(File configFile, BalmConfigData configData) throws IOException {
        var categories = new HashMap<String, Map<String, Object>>();
        var comments = new HashMap<String, String>();
        for (Field categoryField : configData.getClass().getFields()) {
            var category = categoryField.getName();
            var categoryComment = categoryField.getAnnotation(Comment.class);
            if (categoryComment != null) {
                comments.put(category, categoryComment.value());
            }
            var properties = new HashMap<String, Object>();
            try {
                var categoryInstance = categoryField.get(configData);
                for (Field propertyField : categoryInstance.getClass().getFields()) {
                    var property = propertyField.getName();
                    var propertyComment = propertyField.getAnnotation(Comment.class);
                    if (propertyComment != null) {
                        comments.put(category + "." + property, propertyComment.value());
                    }
                    var value = propertyField.get(categoryInstance);
                    properties.put(property, value);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            categories.put(category, properties);
        }
        try (FileWriter writer = new FileWriter(configFile)) {
            NotomlSerializer.serialize(writer, categories, comments);
        }
    }
}
