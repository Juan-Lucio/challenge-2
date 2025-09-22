package org.edu.university.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * JsonReader - Reads JSON files and flattens records into key-value maps.
 *
 * Example:
 * { "user": { "name": "Alice" } }
 * → { "user.name": "Alice" }
 */
public class JsonReader {

    private final ObjectMapper mapper = new ObjectMapper();

    /** Reads and flattens JSON records */
    public List<Map<String, String>> readAndFlatten(String inputPath) throws IOException {
        File file = new File(inputPath);
        if (!file.exists()) throw new IOException("File not found: " + inputPath);

        JsonNode root = mapper.readTree(file);

        List<JsonNode> records = new ArrayList<>();
        if (root.isArray()) root.forEach(records::add);
        else if (root.isObject()) records = Collections.singletonList(root);
        else throw new IOException("Invalid format: root must be object or array.");

        List<Map<String, String>> result = new ArrayList<>();
        for (JsonNode record : records) {
            Map<String, String> flat = new LinkedHashMap<>();
            flattenNode("", record, flat);
            result.add(flat);
        }
        return result;
    }

    /** Recursive flattening: nested keys → dot notation */
    private void flattenNode(String prefix, JsonNode node, Map<String, String> out) throws JsonProcessingException {
        if (node.isObject()) {
            node.fields().forEachRemaining(e -> {
                String key = prefix.isEmpty() ? e.getKey() : prefix + "." + e.getKey();
                try {
                    flattenNode(key, e.getValue(), out);
                } catch (JsonProcessingException ex) {
                    throw new RuntimeException(ex);
                }
            });
        } else if (node.isArray()) {
            out.put(prefix, mapper.writeValueAsString(node));
        } else if (node.isNull()) {
            out.put(prefix, "");
        } else {
            out.put(prefix, node.asText());
        }
    }
}
