package me.artaphy.axliumcore.config.validation.rules;

import me.artaphy.axliumcore.config.validation.ValidationRule;
import me.artaphy.axliumcore.config.validation.ValidationResult;
import org.bukkit.configuration.ConfigurationSection;

import java.util.regex.Pattern;

/**
 * Validates string values against regular expression patterns
 * <p>
 * This rule supports:
 * <ul>
 *     <li>Regular expression pattern matching</li>
 *     <li>Format validation (email, URL, etc.)</li>
 *     <li>Custom pattern constraints</li>
 *     <li>Detailed pattern mismatch reporting</li>
 * </ul>
 * 
 * Usage example:
 * <pre>
 * validator.addRule(Rules.pattern("email", "^[A-Za-z0-9+_.-]+@(.+)$"));
 * validator.addRule(Rules.pattern("username", "^[a-zA-Z0-9_]{3,16}$"));
 * </pre>
 *
 * @author Artaphy
 * @version 1.0
 * @since 1.0
 */
public class PatternValidationRule implements ValidationRule {
    private final String path;
    private final Pattern pattern;

    public PatternValidationRule(String path, String regex) {
        this.path = path;
        this.pattern = Pattern.compile(regex);
    }

    @Override
    public ValidationResult validate(ConfigurationSection config, String basePath) {
        ValidationResult result = new ValidationResult();
        
        if (config.contains(path)) {
            String value = config.getString(path);
            if (value != null && !pattern.matcher(value).matches()) {
                result.addError(basePath + "." + path, 
                    String.format("Value must match pattern: %s", pattern.pattern()));
            }
        }
        
        return result;
    }
} 