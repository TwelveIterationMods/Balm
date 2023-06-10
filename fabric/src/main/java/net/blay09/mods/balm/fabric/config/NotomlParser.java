package net.blay09.mods.balm.fabric.config;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotomlParser {
    public static Map<String, Map<String, Object>> parseProperties(Reader reader) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(reader);
        String line;
        Map<String, Map<String, Object>> categoryMap = new HashMap<>();
        String currentCategory = "";

        int lineNumber = 0;
        while ((line = bufferedReader.readLine()) != null) {
            lineNumber++;
            line = line.trim();
            if (line.startsWith("#") || line.isEmpty()) {
                continue;
            }
            try {
                if (line.startsWith("[") && line.endsWith("]")) {
                    currentCategory = line.substring(1, line.length() - 1);
                    categoryMap.put(currentCategory, new HashMap<>());
                } else if (line.contains("=")) {
                    String[] parts = line.split("=", 2);
                    if (parts.length < 2) {
                        throw new IllegalArgumentException("Expected a key and a value separated by '='");
                    }
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    Map<String, Object> propertiesMap = categoryMap.get(currentCategory);
                    propertiesMap.put(key, parseValue(value));
                } else {
                    throw new IllegalArgumentException("Expected a key and a value separated by '='");
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("Error parsing line " + lineNumber + ": " + e.getMessage());
            }
        }

        bufferedReader.close();
        return categoryMap;
    }

    private static Object parseValue(String value) {
        if (value.startsWith("[") && value.endsWith("]")) {
            return parseList(value);
        } else if (value.equals("true") || value.equals("false")) {
            return Boolean.parseBoolean(value);
        } else {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e1) {
                try {
                    return Double.parseDouble(value);
                } catch (NumberFormatException e2) {
                    if ((value.startsWith("\"") && value.endsWith("\"")) || (value.startsWith("'") && value.endsWith("'"))) {
                        return value.substring(1, value.length() - 1);
                    } else {
                        throw new IllegalArgumentException("Expected a number or a string enclosed in quotes");
                    }
                }
            }
        }
    }

    private static List<Object> parseList(String value) {
        List<Object> list = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        boolean inQuotes = false;
        boolean escapeNext = false;

        // Remove opening and closing brackets
        String trimmedValue = value.substring(1, value.length() - 1).trim();

        for (char ch : trimmedValue.toCharArray()) {
            if (!inQuotes && ch == ',') {
                // Add parsed value to the list and reset the builder for the next value
                list.add(parseValue(builder.toString().trim()));
                builder.setLength(0);
            } else if (ch == '"' && !escapeNext) {
                // Toggle quotes flag
                inQuotes = !inQuotes;
                builder.append(ch);
            } else if (ch == '\\' && !escapeNext) {
                // The next character is escaped
                escapeNext = true;
            } else {
                escapeNext = false;
                builder.append(ch);
            }
        }

        if (inQuotes) {
            throw new IllegalArgumentException("Unclosed quote in list");
        }

        // Don't forget the last value
        if (builder.length() > 0) {
            list.add(parseValue(builder.toString().trim()));
        }

        return list;
    }
}
