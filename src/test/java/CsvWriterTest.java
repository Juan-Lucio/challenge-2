import com.opencsv.CSVWriter;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class CsvWriterTest {

    @Test
    void testWriteMultipleCsvFiles() throws IOException {
        // Crear 10 archivos de prueba
        for (int i = 1; i <= 10; i++) {
            String outputPath = "target/test-csv-writer-" + i + ".csv";
            File file = new File(outputPath);

            try (CSVWriter writer = new CSVWriter(new FileWriter(file))) {
                // Encabezados
                writer.writeNext(new String[]{"Name", "Class", "Marks"});

                // Datos de ejemplo
                writer.writeNext(new String[]{"Aman", "10", "620"});
                writer.writeNext(new String[]{"Suraj", "10", "630"});
                writer.writeNext(new String[]{"TestUser" + i, "12", String.valueOf(500 + i)});
            }

            // Validaciones
            assertTrue(file.exists(), "❌ El archivo " + file.getName() + " no fue creado.");
            assertTrue(file.length() > 0, "❌ El archivo " + file.getName() + " está vacío.");

            System.out.println("✅ Archivo generado y validado: " + file.getAbsolutePath());
        }
    }
}
