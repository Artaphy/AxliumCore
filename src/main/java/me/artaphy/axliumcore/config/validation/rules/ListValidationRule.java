package me.artaphy.axliumcore.config.validation.rules;

import me.artaphy.axliumcore.config.validation.ValidationRule;
import me.artaphy.axliumcore.config.validation.ValidationResult;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

/**
 * Validates list configurations in YAML files
 * <p>
 * This class provides validation for:
 * <ul>
 *     <li>List size constraints (minimum and maximum)</li>
 *     <li>Element type checking</li>
 *     <li>Null value handling</li>
 *     <li>Index-based error reporting</li>
 * </ul>
 * 
 * Usage example:
 * <pre>
 * validator.addRule(Rules.list("permissions", 1, 10, String.class));
 * validator.addRule(Rules.list("worlds", null, 5, String.class));
 * </pre>
 *
 * @author Artaphy
 * @version 1.0
 * @since 1.0
 */
public class ListValidationRule implements ValidationRule {
    private final String path;
    private final Integer minSize;
    private final Integer maxSize;
    private final Class<?> elementType;

    public ListValidationRule(String path, Integer minSize, Integer maxSize, Class<?> elementType) {
        this.path = path;
        this.minSize = minSize;
        this.maxSize = maxSize;
        this.elementType = elementType;
    }

    @Override
    public ValidationResult validate(ConfigurationSection config, String basePath) {
        ValidationResult result = new ValidationResult();
        
        if (config.contains(path)) {
            List<?> list = config.getList(path);
            if (list == null) {
                result.addError(basePath + "." + path, "Value must be a list");
                return result;
            }

            // Validate size
            if (minSize != null && list.size() < minSize) {
                result.addError(basePath + "." + path, 
                    String.format("List must have at least %d elements", minSize));
            }
            if (maxSize != null && list.size() > maxSize) {
                result.addError(basePath + "." + path, 
                    String.format("List must have at most %d elements", maxSize));
            }

            // Validate element types
            if (elementType != null) {
                for (int i = 0; i < list.size(); i++) {
                    Object element = list.get(i);
                    if (element != null && !elementType.isInstance(element)) {
                        result.addError(basePath + "." + path + "[" + i + "]",
                            String.format("Element must be of type %s", elementType.getSimpleName()));
                    }
                }
            }
        }
        
        return result;
    }
} 