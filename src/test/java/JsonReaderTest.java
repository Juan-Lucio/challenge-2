import org.edu.university.converter.JsonReader;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JsonReaderTest {

    @Test
    void testReadJson() throws IOException {
        // 1. Crear archivo JSON temporal dentro de target/
        String outputPath = "target/test-sample.json";

        String sampleJson = """
        [
          {
            "id": 1,
            "author": "Beatriz Solórzano",
            "department": "Scientometrics",
            "publication": {
              "title": "Data Integration in Higher Education",
              "year": 2023,
              "journal": "Journal of Research Analytics"
            },
            "keywords": ["Java", "JSON", "CSV", "Automation"],
            "citations": 15
          },
          {
            "id": 2,
            "author": "Juan Pérez",
            "department": "Scientometrics",
            "publication": {
              "title": "Automation of Scientific Reports",
              "year": 2024,
              "journal": "University Science Review"
            },
            "keywords": ["Scrum", "Software Engineering"],
            "citations": 8
          }
        ]
        """;

        try (FileWriter writer = new FileWriter(outputPath)) {
            writer.write(sampleJson);
        }

        // 2. Leer JSON usando JsonReader
        JsonReader reader = new JsonReader();
        List<Map<String, String>> records = reader.readAndFlatten(outputPath);


        //3. Validaciones
        assertNotNull(records, "❌ La lista de registros no debe ser null.");
        assertEquals(2, records.size(), "❌ Deben existir 2 registros en el JSON.");

        // Validar el primer autor
        assertEquals("Beatriz Solórzano", records.get(0).get("author"));

        // Validar la segunda publicación (aplanada!)
        assertEquals("Automation of Scientific Reports", records.get(1).get("publication.title"));


        System.out.println("✅ JsonReader test passed! Archivo procesado: " + outputPath);
    }
}
