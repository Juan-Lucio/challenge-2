package org.edu.university.converter;

import com.opencsv.CSVWriter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

/**
 * Escribe una lista de filas aplanadas en un archivo CSV.
 */
public class CsvWriter2 {

    /**
     * Escribe CSV con el delimitador especificado.
     *
     * @param outputPath ruta del CSV de salida
     * @param rows lista de filas aplanadas (LinkedHashMap para orden)
     * @param delimiter carácter separador (ejemplo: ',' o ';' o '\t')
     * @throws IOException si ocurre error de escritura
     */
    public void writeCsv(String outputPath, List<Map<String, String>> rows, char delimiter) throws IOException {
        if (rows == null || rows.isEmpty()) {
            throw new IllegalArgumentException("No hay datos para escribir en CSV.");
        }

        // Calcular headers: unión de todas las claves (orden de aparición)
        LinkedHashSet<String> headers = new LinkedHashSet<>();
        for (Map<String, String> row : rows) {
            headers.addAll(row.keySet());
        }
        String[] headerArr = headers.toArray(new String[0]);

        Path out = Paths.get(outputPath);
        Path parent = out.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }

        // Intentar crear un archivo nuevo, lanzar error si ya existe
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(out, StandardCharsets.UTF_8, StandardOpenOption.CREATE_NEW);
             CSVWriter writer = new CSVWriter(bufferedWriter, delimiter,
                     CSVWriter.DEFAULT_QUOTE_CHARACTER,
                     CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                     CSVWriter.DEFAULT_LINE_END)) {

            // Escribir encabezados
            writer.writeNext(headerArr);

            // Escribir filas
            for (Map<String, String> row : rows) {
                String[] values = new String[headerArr.length];
                for (int i = 0; i < headerArr.length; i++) {
                    values[i] = row.getOrDefault(headerArr[i], "");
                }
                writer.writeNext(values);
            }

        } catch (FileAlreadyExistsException e) {
            throw new IOException("❌ El archivo ya existe: " + outputPath + ". Por favor, elige otro nombre o elimínalo manualmente.");
        }
    }
}
