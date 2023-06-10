package net.blay09.mods.balm.fabric.config;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;

public class NotomlSerializer {

    public static void serialize(Writer writer, Map<String, Map<String, Object>> data, Map<String, String> comments) throws IOException {
        writer.write(serializeToString(data, comments));
    }

    private static String serializeToString(Map<String, Map<String, Object>> data, Map<String, String> comments) {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, Map<String, Object>> category : data.entrySet()) {
            String categoryComment = comments.get(category.getKey());
            if (categoryComment != null) {
                sb.append("# ").append(categoryComment).append("\n");
            }

            sb.append("[").append(category.getKey()).append("]").append("\n");

            for (Map.Entry<String, Object> entry : category.getValue().entrySet()) {
                String propertyComment = comments.get(category.getKey() + "." + entry.getKey());
                if (propertyComment != null) {
                    sb.append("# ").append(propertyComment).append("\n");
                }

                sb.append(entry.getKey()).append(" = ");

                if (entry.getValue() instanceof String) {
                    sb.append("\"")
                            .append(entry.getValue())
                            .append("\"");
                } else if (entry.getValue() instanceof List<?>) {
                    sb.append(listToString((List<?>) entry.getValue()));
                } else {
                    sb.append(entry.getValue());
                }

                sb.append("\n");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private static String listToString(List<?> list) {
        StringBuilder sb = new StringBuilder();
        sb.append("[ ");
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) instanceof String) {
                sb.append("\"").append(((String) list.get(i)).replace("\"", "\\\"")).append("\"");
            } else {
                sb.append(list.get(i));
            }
            if (i != list.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append(" ]");
        return sb.toString();
    }

}
