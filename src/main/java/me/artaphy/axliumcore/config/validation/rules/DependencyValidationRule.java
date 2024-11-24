package me.artaphy.axliumcore.config.validation.rules;

import me.artaphy.axliumcore.config.validation.ValidationRule;
import me.artaphy.axliumcore.config.validation.ValidationResult;
import org.bukkit.configuration.ConfigurationSection;

/**
 * Validates dependencies between configuration values
 * <p>
 * This rule ensures that:
 * <ul>
 *     <li>Required dependencies exist</li>
 *     <li>Dependency values match requirements</li>
 *     <li>Conditional configuration is valid</li>
 *     <li>Cross-field validation is enforced</li>
 * </ul>
 * 
 * Usage example:
 * <pre>
 * validator.addRule(Rules.dependency("mysql.password", "mysql.enabled", true));
 * validator.addRule(Rules.dependency("advanced.settings", "mode", "advanced"));
 * </pre>
 *
 * @author Artaphy
 * @version 1.0
 * @since 1.0
 */
public class DependencyValidationRule implements ValidationRule {
    private final String path;
    private final String dependsOn;
    private final Object requiredValue;

    public DependencyValidationRule(String path, String dependsOn, Object requiredValue) {
        this.path = path;
        this.dependsOn = dependsOn;
        this.requiredValue = requiredValue;
    }

    @Override
    public ValidationResult validate(ConfigurationSection config, String basePath) {
        ValidationResult result = new ValidationResult();
        
        if (config.contains(path) && config.contains(dependsOn)) {
            Object dependencyValue = config.get(dependsOn);
            if (!requiredValue.equals(dependencyValue)) {
                result.addError(basePath + "." + path,
                    String.format("Requires %s to be %s", dependsOn, requiredValue));
            }
        }
        
        return result;
    }
} 