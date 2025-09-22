package org.edu.university.converter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * JsonToCsvConverter - Orchestrates the conversion from JSON to CSV.
 */
public class JsonToCsvConverter {

    private final JsonReader jsonReader;
    private final CsvWriter2 csvWriter;

    public JsonToCsvConverter() {
        this.jsonReader = new JsonReader();
        this.csvWriter = new CsvWriter2();
    }

    /**
     * Converts JSON file into CSV file with a specified delimiter.
     *
     * @return number of rows processed
     */
    public int convert(String inputPath, String outputPath, char delimiter) throws IOException {
        List<Map<String, String>> data = jsonReader.readAndFlatten(inputPath);
        csvWriter.writeCsv(outputPath, data, delimiter);
        return data.size();
    }
}
