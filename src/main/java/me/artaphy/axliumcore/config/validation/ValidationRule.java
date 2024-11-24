package me.artaphy.axliumcore.config.validation;

import org.bukkit.configuration.ConfigurationSection;

/**
 * Interface for configuration validation rules
 * <p>
 * This interface provides:
 * <ul>
 *     <li>Standardized validation method</li>
 *     <li>Path-based validation support</li>
 *     <li>Flexible validation results</li>
 *     <li>Section-level validation</li>
 * </ul>
 * 
 * Implementation guidelines:
 * <ul>
 *     <li>Handle null values gracefully</li>
 *     <li>Provide clear error messages</li>
 *     <li>Support nested paths</li>
 *     <li>Be thread-safe</li>
 * </ul>
 *
 * @author Artaphy
 * @version 1.0
 * @since 1.0
 */
public interface ValidationRule {
    /**
     * Validate a configuration section against this rule
     * @param config Configuration to validate
     * @param path Path to validate
     * @return Validation result
     */
    ValidationResult validate(ConfigurationSection config, String path);
} 