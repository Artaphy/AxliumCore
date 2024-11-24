package me.artaphy.axliumcore.config.validation.rules;

import me.artaphy.axliumcore.config.validation.ValidationRule;
import me.artaphy.axliumcore.config.validation.ValidationResult;
import org.bukkit.configuration.ConfigurationSection;

/**
 * Validates configuration value types
 * <p>
 * This rule ensures that:
 * <ul>
 *     <li>Values match expected types</li>
 *     <li>Type mismatches are reported</li>
 *     <li>Null values are handled</li>
 *     <li>Detailed error messages are provided</li>
 * </ul>
 * 
 * Usage example:
 * <pre>
 * validator.addRule(Rules.type("server.port", Integer.class));
 * validator.addRule(Rules.type("server.name", String.class));
 * validator.addRule(Rules.type("server.debug", Boolean.class));
 * </pre>
 *
 * @author Artaphy
 * @version 1.0
 * @since 1.0
 */
public class TypeValidationRule implements ValidationRule {
    private final String path;
    private final Class<?> expectedType;

    public TypeValidationRule(String path, Class<?> expectedType) {
        this.path = path;
        this.expectedType = expectedType;
    }

    @Override
    public ValidationResult validate(ConfigurationSection config, String basePath) {
        ValidationResult result = new ValidationResult();
        
        if (config.contains(path)) {
            Object value = config.get(path);
            if (value != null && !expectedType.isInstance(value)) {
                result.addError(basePath + "." + path, 
                    String.format("Expected type %s but got %s", 
                        expectedType.getSimpleName(), 
                        value.getClass().getSimpleName()));
            }
        }
        
        return result;
    }
} 