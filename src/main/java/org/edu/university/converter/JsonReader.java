package org.edu.university.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Lee archivos JSON y devuelve una lista de mapas con claves aplanadas.
 */
public class JsonReader {

    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Lee y aplan a los registros desde el JSON.
     * @param inputPath ruta al archivo JSON
     * @return lista de filas (map key->value) aplanadas
     * @throws IOException si archivo no encontrado o JSON inválido
     */

    public List<Map<String, String>> readAndFlatten(String inputPath) throws IOException {
        File file = new File(inputPath);
        if (!file.exists()) throw new IOException("Archivo no encontrado: " + inputPath);

        JsonNode root = mapper.readTree(file);
        List<JsonNode> records = new ArrayList<>();

        if (root.isArray()) {
            root.forEach(records::add);
        } else if (root.isObject()) {
            records.add(root);
        } else {
            throw new IOException("Formato inválido: el JSON raíz debe ser un objeto o un array de objetos.");
        }

        List<Map<String, String>> result = new ArrayList<>();
        for (JsonNode record : records) {
            Map<String, String> flat = new LinkedHashMap<>();
            flattenNode("", record, flat);
            result.add(flat);
        }
        return result;
    }

    /**
     * Aplana recursivamente un JsonNode en el mapa out con claves separadas por puntos.
     * Arrays se convierten a JSON string.
     */
    private void flattenNode(String prefix, JsonNode node, Map<String, String> out) throws JsonProcessingException {
        if (node.isObject()) {
            Iterator<Map.Entry<String, JsonNode>> it = node.fields();
            while (it.hasNext()) {
                Map.Entry<String, JsonNode> e = it.next();
                String key = prefix.isEmpty() ? e.getKey() : prefix + "." + e.getKey();
                flattenNode(key, e.getValue(), out);
            }
        } else if (node.isArray()) {
            // Convertimos arrays a string JSON para conservar la información
            String value = mapper.writeValueAsString(node);
            out.put(prefix, value);
        } else if (node.isNull()) {
            out.put(prefix, "");
        } else {
            out.put(prefix, node.asText());
        }
    }
}
