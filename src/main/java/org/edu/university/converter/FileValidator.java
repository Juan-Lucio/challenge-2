package org.edu.university.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * FileValidator
 *
 * This class centralizes validation logic for JSON input files.
 * It provides:
 *  - isValidJsonFile(path): simple boolean check (keeps compatibility with existing code).
 *  - validateJsonFile(path): performs full checks and prints descriptive error messages to System.err.
 *  - validateOrThrow(path): same as validateJsonFile but throws ValidationException on error.
 *
 * Important: this class only *validates* the JSON file (existence, readability, basic JSON syntax).
 * It does NOT perform any conversion or other side effects.
 */
public class FileValidator {

    // Reusable Jackson ObjectMapper for light-weight syntax checking.
    // ObjectMapper is thread-safe for read operations after configuration.
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Legacy-friendly quick check used by MainApp in previous versions.
     * Returns true if path exists, is a file, and ends with .json (case-insensitive).
     * This method does NOT validate JSON syntax; it only checks file presence and extension.
     *
     * @param path candidate file path
     * @return true if file exists and has .json extension; false otherwise
     */
    public static boolean isValidJsonFile(String path) {
        if (path == null || path.isBlank()) {
            // Path is missing or empty -> invalid.
            return false;
        }
        File f = new File(path);
        // exists() -> file is present; isFile() -> it's not a directory.
        // endsWith(".json") ensures user passed a .json file (helps early UX).
        return f.exists() && f.isFile() && path.toLowerCase().endsWith(".json");
    }

    /**
     * Validate the JSON file with several checks and print human-friendly error messages
     * to System.err when validation fails. Returns true when the file passes all checks.
     *
     * Checks performed (in order):
     *  1) path not null/blank
     *  2) file exists and is a regular file
     *  3) filename ends with .json
     *  4) file is readable and size > 0 (not empty)
     *  5) basic JSON syntax check using Jackson (ObjectMapper.readTree)
     *
     * This method does not throw on parse errors — it prints an informative message and returns false.
     *
     * @param path candidate file path
     * @return true if file passes all validation steps; false otherwise
     */
    public static boolean validateJsonFile(String path) {
        // 1) null / blank check
        if (path == null || path.isBlank()) {
            System.err.println("Validation error: no file path provided.");
            return false;
        }

        // 2) existence and file type
        File file = new File(path);
        if (!file.exists()) {
            System.err.println("Validation error: file not found -> " + path);
            return false;
        }
        if (!file.isFile()) {
            System.err.println("Validation error: path is not a regular file -> " + path);
            return false;
        }

        // 3) extension check
        if (!path.toLowerCase().endsWith(".json")) {
            System.err.println("Validation error: file does not have a .json extension -> " + path);
            return false;
        }

        // 4) readability and non-empty
        Path p = file.toPath();
        try {
            // isReadable is a basic platform check whether we can read the file
            if (!Files.isReadable(p)) {
                System.err.println("Validation error: file is not readable (permissions?) -> " + path);
                return false;
            }
            long size = Files.size(p);
            if (size == 0L) {
                System.err.println("Validation error: file is empty -> " + path);
                return false;
            }
        } catch (IOException ioEx) {
            // Could not access file metadata for some reason
            System.err.println("Validation error: unable to access file metadata -> " + path);
            System.err.println("  detail: " + ioEx.getMessage());
            return false;
        }

        // 5) basic JSON syntax check using Jackson
        try {
            // Attempt to parse the file into a JsonNode tree.
            // readTree will throw a JsonProcessingException (or IO) when syntax is invalid.
            JsonNode root = MAPPER.readTree(file);

            // Basic structural check: root must be either an Object or an Array for our application.
            if (!(root.isObject() || root.isArray())) {
                System.err.println("Validation error: JSON root is not an object or array. " +
                        "Expected a JSON object or an array of objects. -> " + path);
                return false;
            }

            // If we reach here, basic JSON syntax & shape is acceptable.
            return true;

        } catch (JsonProcessingException jpe) {
            // Jackson found malformed JSON syntax: provide the message to the user.
            System.err.println("Validation error: invalid JSON syntax in file -> " + path);
            System.err.println("  parser message: " + jpe.getOriginalMessage());
            return false;
        } catch (IOException ioe) {
            // Some other I/O error occurred during reading (rare after earlier checks).
            System.err.println("Validation error: I/O error while reading file -> " + path);
            System.err.println("  detail: " + ioe.getMessage());
            return false;
        }
    }

    /**
     * Validate the JSON file and throw a ValidationException on failure.
     * Use this method when you want exception-driven flow (for example, in unit tests or CLI with error exit codes).
     *
     * @param path candidate file path
     * @throws ValidationException when validation fails with a descriptive message
     */
    public static void validateOrThrow(String path) throws ValidationException {
        // Reuse validateJsonFile to get messages printed and a boolean result.
        boolean ok = validateJsonFile(path);
        if (!ok) {
            // If validation failed, throw a ValidationException with a short generic message.
            // Detailed messages were already printed to System.err by validateJsonFile.
            throw new ValidationException("JSON file validation failed for path: " + path);
        }
    }

    /**
     * Simple custom exception to represent validation failures when throwing is preferred.
     */
    public static class ValidationException extends Exception {
        public ValidationException(String message) {
            super(message);
        }
    }

    /**
     * Usage examples (do not run here):
     *
     * // 1) Quick legacy boolean check
     * if (!FileValidator.isValidJsonFile(inputPath)) {
     *     System.err.println("Please provide a .json file.");
     *     return;
     * }
     *
     * // 2) Full validation with messages (preferred for CLI)
     * if (!FileValidator.validateJsonFile(inputPath)) {
     *     // validateJsonFile already printed the error details to System.err
     *     return;
     * }
     *
     * // 3) Exception-driven flow
     * try {
     *     FileValidator.validateOrThrow(inputPath);
     * } catch (FileValidator.ValidationException e) {
     *     System.err.println("Validation failed: " + e.getMessage());
     *     return;
     * }
     */
}
