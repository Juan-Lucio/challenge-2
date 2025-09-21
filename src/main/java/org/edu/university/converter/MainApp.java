package org.edu.university.converter;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Punto de entrada. Soporta:
 * --input=<ruta>  (o primer argumento posicional)
 * --output=<ruta>
 * --delimiter=<caracter>  (ej: "," ";" "\t")
 * --no-prompt  (para no preguntar interactivamente)
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

        if (!noPrompt && (args.length == 0 || (!options.containsKey("input") && !options.containsKey("pos0")))) {
            System.out.println("⚡ Bienvenido al convertidor JSON → CSV ⚡");
            System.out.println("No se detectaron argumentos, se usarán valores por defecto:");
            System.out.println("Entrada: " + "C:\\Users\\DELL\\IdeaProjects\\JSON to CSV\\src\\main\\resources\\sample.json");
            System.out.println("Salida:  " + outputPath);
            System.out.println("Delimitador: '" + delimiter + "'");
            System.out.println("¿Deseas usar otras rutas o configuraciones? (s/n)");
            Scanner scanner = new Scanner(System.in);
            String answer = scanner.nextLine().trim().toLowerCase();
            if (answer.equals("s")) {
                System.out.print("👉 Ruta del archivo JSON de entrada: ");
                String in = scanner.nextLine().trim();
                if (!in.isBlank()) inputPath = in;

                System.out.print("👉 Ruta del archivo CSV de salida: ");
                String out = scanner.nextLine().trim();
                if (!out.isBlank()) outputPath = out;

                //System.out.print("👉 Delimitador (un solo carácter, ej: , o ; o \\t): ");
               // String del = scanner.nextLine().trim();
               // if (!del.isBlank()) delimiter = del.charAt(0);
            }
        }

        // Validación básica
        if (!FileValidator.isValidJsonFile(inputPath)) {
            System.err.println("❌ Error: no se encontró un archivo JSON válido en: " + inputPath);
            System.err.println("Asegúrate que la ruta exista y termine en .json");
            return;
        }

        // Ejecutar conversión
        JsonToCsvConverter converter = new JsonToCsvConverter();
        try {
            int rows = converter.convert(inputPath, outputPath, delimiter);
            System.out.println("✅ Conversión completada. Filas procesadas: " + rows);
            System.out.println("Archivo generado en: " + outputPath);
        } catch (Exception e) {
            System.err.println("❌ Ocurrió un error durante la conversión: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Parseo simple de argumentos:
     * - acepta --input=...
     * - acepta primer argumento posicional (pos0)
     * - acepta --no-prompt
     */
    private static Map<String, String> parseArgs(String[] args) {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < args.length; i++) {
            String a = args[i];
            if (a.startsWith("--input=")) map.put("input", a.substring("--input=".length()));
            else if (a.startsWith("--output=")) map.put("output", a.substring("--output=".length()));
            else if (a.startsWith("--delimiter=")) map.put("delimiter", a.substring("--delimiter=".length()));
            else if (a.equals("--no-prompt")) map.put("no-prompt", "true");
            else if (i == 0) map.put("pos0", a); // permitir primer arg posicional como input
        }
        return map;
    }
}
