package me.artaphy.axliumcore.config.validation.rules;

import me.artaphy.axliumcore.config.validation.ValidationRule;
import me.artaphy.axliumcore.config.validation.ValidationResult;
import org.bukkit.configuration.ConfigurationSection;

/**
 * Validates required configuration fields
 * <p>
 * This rule ensures that:
 * <ul>
 *     <li>Required paths exist in configuration</li>
 *     <li>Missing fields are reported</li>
 *     <li>Multiple paths can be validated</li>
 *     <li>Nested paths are supported</li>
 * </ul>
 * 
 * Usage example:
 * <pre>
 * validator.addRule(Rules.required(
 *     "database.host",
 *     "database.port",
 *     "database.username"
 * ));
 * </pre>
 *
 * @author Artaphy
 * @version 1.0
 * @since 1.0
 */
public class RequiredFieldRule implements ValidationRule {
    private final String[] requiredPaths;

    public RequiredFieldRule(String... paths) {
        this.requiredPaths = paths;
    }

    @Override
    public ValidationResult validate(ConfigurationSection config, String path) {
        ValidationResult result = new ValidationResult();
        
        for (String required : requiredPaths) {
            if (!config.contains(required)) {
                result.addError(path + "." + required, "Required field is missing");
            }
        }
        
        return result;
    }
} 