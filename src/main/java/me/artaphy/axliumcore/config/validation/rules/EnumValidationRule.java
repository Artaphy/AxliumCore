package me.artaphy.axliumcore.config.validation.rules;

import me.artaphy.axliumcore.config.validation.ValidationRule;
import me.artaphy.axliumcore.config.validation.ValidationResult;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Validates string values against a predefined set of options
 * <p>
 * Features:
 * <ul>
 *     <li>Case-sensitive or case-insensitive matching</li>
 *     <li>Multiple allowed values support</li>
 *     <li>Detailed error messages with valid options</li>
 *     <li>Null value handling</li>
 * </ul>
 * 
 * Usage example:
 * <pre>
 * validator.addRule(Rules.enumValue("gamemode", false, "survival", "creative", "adventure"));
 * validator.addRule(Rules.enumValue("difficulty", true, "EASY", "NORMAL", "HARD"));
 * </pre>
 *
 * @author Artaphy
 * @version 1.0
 * @since 1.0
 */
public class EnumValidationRule implements ValidationRule {
    private final String path;
    private final Set<String> allowedValues;
    private final boolean caseSensitive;

    public EnumValidationRule(String path, boolean caseSensitive, String... allowedValues) {
        this.path = path;
        this.caseSensitive = caseSensitive;
        this.allowedValues = Arrays.stream(allowedValues)
            .map(v -> caseSensitive ? v : v.toLowerCase())
            .collect(Collectors.toSet());
    }

    @Override
    public ValidationResult validate(ConfigurationSection config, String basePath) {
        ValidationResult result = new ValidationResult();
        
        if (config.contains(path)) {
            String value = config.getString(path);
            if (value != null) {
                String checkValue = caseSensitive ? value : value.toLowerCase();
                if (!allowedValues.contains(checkValue)) {
                    result.addError(basePath + "." + path,
                        String.format("Value must be one of: %s", 
                            String.join(", ", allowedValues)));
                }
            }
        }
        
        return result;
    }
} 