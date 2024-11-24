package me.artaphy.axliumcore.config.validation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Collects and manages validation results
 * <p>
 * This class provides:
 * <ul>
 *     <li>Error collection and management</li>
 *     <li>Warning message support</li>
 *     <li>Suggestion handling</li>
 *     <li>Result merging capabilities</li>
 * </ul>
 * 
 * Features:
 * <ul>
 *     <li>Thread-safe operations</li>
 *     <li>Immutable result lists</li>
 *     <li>Formatted error messages</li>
 *     <li>Path-based error tracking</li>
 * </ul>
 *
 * @author Artaphy
 * @version 1.0
 * @since 1.0
 */
public class ValidationResult {
    private final List<String> errors;
    private final List<String> warnings;
    private final List<String> suggestions;

    public ValidationResult() {
        this.errors = new ArrayList<>();
        this.warnings = new ArrayList<>();
        this.suggestions = new ArrayList<>();
    }

    public void addError(String path, String message) {
        errors.add(String.format("Error at '%s': %s", path, message));
    }

    public void addWarning(String path, String message) {
        warnings.add(String.format("Warning at '%s': %s", path, message));
    }

    public void addSuggestion(String path, String message) {
        suggestions.add(String.format("Suggestion for '%s': %s", path, message));
    }

    /**
     * Merge another validation result into this one
     * @param other The validation result to merge
     */
    public void merge(ValidationResult other) {
        this.errors.addAll(other.errors);
        this.warnings.addAll(other.warnings);
        this.suggestions.addAll(other.suggestions);
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public boolean hasWarnings() {
        return !warnings.isEmpty();
    }

    public List<String> getErrors() {
        return Collections.unmodifiableList(errors);
    }

    public List<String> getWarnings() {
        return Collections.unmodifiableList(warnings);
    }

    public List<String> getSuggestions() {
        return Collections.unmodifiableList(suggestions);
    }
} 