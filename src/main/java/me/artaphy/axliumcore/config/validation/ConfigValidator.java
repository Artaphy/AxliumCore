package me.artaphy.axliumcore.config.validation;

import me.artaphy.axliumcore.config.validation.rules.*;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration validation system with rule management
 * <p>
 * This class provides methods to:
 * <ul>
 *     <li>Register validation rules</li>
 *     <li>Validate configuration sections</li>
 *     <li>Process validation results</li>
 *     <li>Handle nested configurations</li>
 * </ul>
 * 
 * Features:
 * <ul>
 *     <li>Fluent rule registration</li>
 *     <li>Recursive validation</li>
 *     <li>Comprehensive rule set</li>
 *     <li>Extensible validation system</li>
 * </ul>
 *
 * @author Artaphy
 * @version 1.0
 * @since 1.0
 */
public class ConfigValidator {
    private final List<ValidationRule> rules;

    public ConfigValidator() {
        this.rules = new ArrayList<>();
    }

    /**
     * Add a validation rule
     * @param rule Rule to add
     * @return this validator
     */
    public ConfigValidator addRule(ValidationRule rule) {
        rules.add(rule);
        return this;
    }

    /**
     * Validate configuration against all rules
     * @param config Configuration to validate
     * @return Validation result
     */
    public ValidationResult validate(ConfigurationSection config) {
        ValidationResult result = new ValidationResult();
        
        for (ValidationRule rule : rules) {
            validateRecursively(config, "", rule, result);
        }
        
        return result;
    }

    private void validateRecursively(ConfigurationSection config, String path, 
                                   ValidationRule rule, ValidationResult result) {
        // Validate current path
        result.merge(rule.validate(config, path));
        
        // Recursively validate all sections
        for (String key : config.getKeys(false)) {
            String currentPath = path.isEmpty() ? key : path + "." + key;
            if (config.isConfigurationSection(key)) {
                validateRecursively(config.getConfigurationSection(key), 
                                  currentPath, rule, result);
            }
        }
    }

    /**
     * Create common validation rules
     */
    public static class Rules {
        public static ValidationRule required(String... paths) {
            return new RequiredFieldRule(paths);
        }

        public static ValidationRule type(String path, Class<?> type) {
            return new TypeValidationRule(path, type);
        }

        public static ValidationRule range(String path, Number min, Number max) {
            return new RangeValidationRule(path, min, max);
        }

        public static ValidationRule pattern(String path, String regex) {
            return new PatternValidationRule(path, regex);
        }

        public static ValidationRule custom(String path, 
                java.util.function.Predicate<Object> validator, String message) {
            return new CustomValidationRule(path, validator, message);
        }

        public static ValidationRule list(String path, Integer minSize, Integer maxSize, Class<?> elementType) {
            return new ListValidationRule(path, minSize, maxSize, elementType);
        }

        public static ValidationRule enumValue(String path, boolean caseSensitive, String... allowedValues) {
            return new EnumValidationRule(path, caseSensitive, allowedValues);
        }

        public static ValidationRule dependency(String path, String dependsOn, Object requiredValue) {
            return new DependencyValidationRule(path, dependsOn, requiredValue);
        }
    }
} 