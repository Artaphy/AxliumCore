package me.artaphy.axliumcore.config.validation.rules;

import me.artaphy.axliumcore.config.validation.ValidationRule;
import me.artaphy.axliumcore.config.validation.ValidationResult;
import org.bukkit.configuration.ConfigurationSection;

/**
 * Validates numeric values within specified ranges
 * <p>
 * This rule ensures that:
 * <ul>
 *     <li>Values are numeric types</li>
 *     <li>Numbers fall within specified bounds</li>
 *     <li>Invalid types are properly reported</li>
 * </ul>
 * 
 * Usage example:
 * <pre>
 * validator.addRule(Rules.range("server.port", 1024, 65535));
 * validator.addRule(Rules.range("player.health", 0, 20));
 * </pre>
 *
 * @author Artaphy
 * @version 1.0
 * @since 1.0
 */
public class RangeValidationRule implements ValidationRule {
    private final String path;
    private final Number min;
    private final Number max;

    public RangeValidationRule(String path, Number min, Number max) {
        this.path = path;
        this.min = min;
        this.max = max;
    }

    @Override
    public ValidationResult validate(ConfigurationSection config, String basePath) {
        ValidationResult result = new ValidationResult();
        
        if (config.contains(path)) {
            Object value = config.get(path);
            if (value instanceof Number) {
                double numValue = ((Number) value).doubleValue();
                if (numValue < min.doubleValue() || numValue > max.doubleValue()) {
                    result.addError(basePath + "." + path, 
                        String.format("Value must be between %s and %s", min, max));
                }
            } else {
                result.addError(basePath + "." + path, "Value must be a number");
            }
        }
        
        return result;
    }
} 