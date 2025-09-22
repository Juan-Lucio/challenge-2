package org.edu.university.converter;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * MainApp - Entry point of the JSON → CSV converter.
 *
 * Features:
 * - Accepts command-line arguments:
 *   --input=<path>      → input JSON file path (or first positional argument).
 *   --output=<path>     → output CSV file path.
 *   --delimiter=<char>  → CSV delimiter ("," ";" "\t").
 *   --no-prompt         → disables interactive prompts.
 *
 * - If no arguments are passed, defaults are used, with an option to interactively
 *   provide custom input/output paths.
 *
 * - Validates input file before conversion.
 * - Executes the conversion using JsonToCsvConverter.
 */
public class MainApp {

    private static final String DEFAULT_INPUT = "src/main/resources/sample.json";
    private static final String DEFAULT_OUTPUT = "exports/output.csv";
    private static final char DEFAULT_DELIMITER = ',';

    public static void main(String[] args) {
        Map<String, String> options = parseArgs(args);

        boolean noPrompt = options.containsKey("no-prompt");

        String inputPath = options.getOrDefault("input", options.getOrDefault("pos0", DEFAULT_INPUT));
        String outputPath = options.getOrDefault("output", DEFAULT_OUTPUT);
        char delimiter = options.containsKey("delimiter") ? options.get("delimiter").charAt(0) : DEFAULT_DELIMITER;

        // Interactive prompt only if --no-prompt is not specified and no arguments are provided
        if (!noPrompt && (args.length == 0 || (!options.containsKey("input") && !options.containsKey("pos0")))) {
            System.out.println("⚡ Welcome to the JSON → CSV Converter ⚡");
            System.out.println("No arguments detected, using default values:");
            System.out.println("Input:  " + DEFAULT_INPUT);
            System.out.println("Output: " + outputPath);
            System.out.println("Delimiter: '" + delimiter + "'");
            System.out.println("Do you want to use different paths or settings? (y/n)");

            try (Scanner scanner = new Scanner(System.in)) { // Auto-close scanner
                String answer = scanner.nextLine().trim().toLowerCase();
                if (answer.equals("y")) {
                    System.out.print("👉 Input JSON file path: ");
                    String in = scanner.nextLine().trim();
                    if (!in.isBlank()) inputPath = in;

                    System.out.print("👉 Output CSV file path: ");
                    String out = scanner.nextLine().trim();
                    if (!out.isBlank()) outputPath = out;
                }
            }
        }

        // Basic validation before conversion
        if (!FileValidator.isValidJsonFile(inputPath)) {
            System.err.println("❌ Error: no valid JSON file found at: " + inputPath);
            System.err.println("Make sure the path exists and ends with .json");
            return;
        }

        // Run conversion
        JsonToCsvConverter converter = new JsonToCsvConverter();
        try {
            int rows = converter.convert(inputPath, outputPath, delimiter);
            System.out.println("✅ Conversion complete. Rows processed: " + rows);
            System.out.println("Generated file: " + outputPath);
        } catch (Exception e) {
            System.err.println("❌ Error during conversion: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Simple command-line argument parser.
     * Supports:
     * - --input=...
     * - --output=...
     * - --delimiter=...
     * - --no-prompt
     * - first positional argument as input path
     */
    private static Map<String, String> parseArgs(String[] args) {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < args.length; i++) {
            String a = args[i];
            if (a.startsWith("--input=")) map.put("input", a.substring("--input=".length()));
            else if (a.startsWith("--output=")) map.put("output", a.substring("--output=".length()));
            else if (a.startsWith("--delimiter=")) map.put("delimiter", a.substring("--delimiter=".length()));
            else if (a.equals("--no-prompt")) map.put("no-prompt", "true");
            else if (i == 0) map.put("pos0", a); // first positional argument as input
        }
        return map;
    }
}
