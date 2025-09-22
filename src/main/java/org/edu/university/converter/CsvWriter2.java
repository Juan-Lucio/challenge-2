package org.edu.university.converter;

import com.opencsv.CSVWriter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

/**
 * CsvWriter2 is responsible for writing a list of flattened JSON rows into a CSV file.
 * Uses OpenCSV for safe and structured CSV writing.
 */
public class CsvWriter2 {

    /**
     * Writes a CSV file with the specified delimiter.
     *
     * @param outputPath Path of the output CSV file
     * @param rows       List of flattened rows (each row is a Map of key-value pairs)
     * @param delimiter  Delimiter character (e.g., ',', ';', or '\t')
     * @throws IOException if an error occurs while writing the file
     */
    public void writeCsv(String outputPath, List<Map<String, String>> rows, char delimiter) throws IOException {
        if (rows == null || rows.isEmpty()) {
            throw new IllegalArgumentException("❌ No data available to write into CSV.");
        }

        // Collect headers: union of all keys (preserve insertion order)
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

        // Create file exclusively (error if file already exists)
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(out, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE_NEW);
             CSVWriter writer = new CSVWriter(bufferedWriter,
                     delimiter,
                     CSVWriter.DEFAULT_QUOTE_CHARACTER,
                     CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                     CSVWriter.DEFAULT_LINE_END)) {

            // Write headers first
            writer.writeNext(headerArr);

            // Write each row
            for (Map<String, String> row : rows) {
                String[] values = new String[headerArr.length];
                for (int i = 0; i < headerArr.length; i++) {
                    values[i] = row.getOrDefault(headerArr[i], "");
                }
                writer.writeNext(values);
            }

        } catch (FileAlreadyExistsException e) {
            throw new IOException("❌ File already exists: " + outputPath +
                    ". Please choose another name or delete it manually.");
        }
    }
}
