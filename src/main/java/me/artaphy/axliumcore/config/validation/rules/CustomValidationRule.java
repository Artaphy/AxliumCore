package me.artaphy.axliumcore.config.validation.rules;

import me.artaphy.axliumcore.config.validation.ValidationRule;
import me.artaphy.axliumcore.config.validation.ValidationResult;
import org.bukkit.configuration.ConfigurationSection;

import java.util.function.Predicate;

/**
 * Provides custom validation logic using predicates
 * <p>
 * This rule allows:
 * <ul>
 *     <li>Custom validation logic via predicates</li>
 *     <li>Flexible error message customization</li>
 *     <li>Complex validation scenarios</li>
 *     <li>Integration with external validation systems</li>
 * </ul>
 * 
 * Usage example:
 * <pre>
 * validator.addRule(Rules.custom("server.name",
 *     value -> value instanceof String && ((String) value).length() <= 32,
 *     "Server name must not exceed 32 characters"));
 * </pre>
 *
 * @author Artaphy
 * @version 1.0
 * @since 1.0
 */
public class CustomValidationRule implements ValidationRule {
    private final String path;
    private final Predicate<Object> validator;
    private final String errorMessage;

    public CustomValidationRule(String path, Predicate<Object> validator, String errorMessage) {
        this.path = path;
        this.validator = validator;
        this.errorMessage = errorMessage;
    }

    @Override
    public ValidationResult validate(ConfigurationSection config, String basePath) {
        ValidationResult result = new ValidationResult();
        
        if (config.contains(path)) {
            Object value = config.get(path);
            if (!validator.test(value)) {
                result.addError(basePath + "." + path, errorMessage);
            }
        }
        
        return result;
    }
} 