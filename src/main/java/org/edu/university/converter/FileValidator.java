package org.edu.university.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * FileValidator - Centralized validation logic for JSON files.
 *
 * Provides:
 * - isValidJsonFile(path): quick legacy-friendly check (existence + extension).
 * - validateJsonFile(path): full check with descriptive error messages.
 * - validateOrThrow(path): throws ValidationException if validation fails.
 */
public class FileValidator {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /** Quick legacy check: file exists, is file, and ends with .json */
    public static boolean isValidJsonFile(String path) {
        if (path == null || path.isBlank()) return false;
        File f = new File(path);
        return f.exists() && f.isFile() && path.toLowerCase().endsWith(".json");
    }

    /** Full validation: path existence, extension, readability, non-empty, JSON syntax */
    public static boolean validateJsonFile(String path) {
        if (path == null || path.isBlank()) {
            System.err.println("Validation error: no file path provided.");
            return false;
        }

        File file = new File(path);
        if (!file.exists()) {
            System.err.println("Validation error: file not found -> " + path);
            return false;
        }
        if (!file.isFile()) {
            System.err.println("Validation error: path is not a regular file -> " + path);
            return false;
        }
        if (!path.toLowerCase().endsWith(".json")) {
            System.err.println("Validation error: file does not have a .json extension -> " + path);
            return false;
        }

        Path p = file.toPath();
        try {
            if (!Files.isReadable(p)) {
                System.err.println("Validation error: file not readable -> " + path);
                return false;
            }
            if (Files.size(p) == 0L) {
                System.err.println("Validation error: file is empty -> " + path);
                return false;
            }
        } catch (IOException ioEx) {
            System.err.println("Validation error: unable to access file -> " + ioEx.getMessage());
            return false;
        }

        try {
            JsonNode root = MAPPER.readTree(file);
            if (!(root.isObject() || root.isArray())) {
                System.err.println("Validation error: JSON root must be object or array -> " + path);
                return false;
            }
            return true;
        } catch (JsonProcessingException jpe) {
            System.err.println("Validation error: invalid JSON syntax -> " + jpe.getOriginalMessage());
            return false;
        } catch (IOException ioe) {
            System.err.println("Validation error: I/O error -> " + ioe.getMessage());
            return false;
        }
    }

    /** Throws exception if validation fails */
    public static void validateOrThrow(String path) throws ValidationException {
        if (!validateJsonFile(path)) {
            throw new ValidationException("JSON file validation failed: " + path);
        }
    }

    public static class ValidationException extends Exception {
        public ValidationException(String message) { super(message); }
    }
}
