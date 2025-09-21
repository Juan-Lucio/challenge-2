package org.edu.university.converter;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JsonToCsvConverterTest {
    @Test
    void testConvert() throws IOException {
        // Crear archivo JSON temporal
        String inputPath = "target/sample-test.json";
        String jsonContent = """
        [
          { "id": 1, "author": "Beatriz Solórzano", "publication": { "title": "Data Integration", "year": 2023 } },
          { "id": 2, "author": "Juan Pérez", "publication": { "title": "Automation Reports", "year": 2024 } }
        ]
        """;
        try (FileWriter fw = new FileWriter(inputPath)) {
            fw.write(jsonContent);
        }

        // Output CSV
        String outputPath = "target/converted-test.csv";

        JsonToCsvConverter converter = new JsonToCsvConverter();
        int rows = converter.convert(inputPath, outputPath, ',');

        File file = new File(outputPath);

        // Assertions
        assertTrue(file.exists(), "Converted CSV should exist");
        assertTrue(file.length() > 0, "Converted CSV should not be empty");
        assertEquals(2, rows, "Debe convertir exactamente 2 registros");

        System.out.println("✅ Conversion successful! CSV generated at: " + file.getAbsolutePath());
    }

}
