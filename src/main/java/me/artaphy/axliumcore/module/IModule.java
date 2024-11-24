package me.artaphy.axliumcore.module;

/**
 * Core interface for modular functionality
 * <p>
 * This interface defines:
 * <ul>
 *     <li>Module lifecycle management</li>
 *     <li>Dependency handling</li>
 *     <li>Version control</li>
 *     <li>State management</li>
 * </ul>
 * 
 * Implementation guidelines:
 * <ul>
 *     <li>Ensure thread-safe operations</li>
 *     <li>Handle exceptions gracefully</li>
 *     <li>Provide clear error messages</li>
 *     <li>Clean up resources properly</li>
 * </ul>
 * 
 * Usage example:
 * <pre>
 * public class DatabaseModule implements IModule {
 *     @Override
 *     public boolean onEnable() {
 *         // Initialize database connection
 *         return connectToDatabase();
 *     }
 *     
 *     @Override
 *     public void onDisable() {
 *         // Close database connection
 *         closeConnection();
 *     }
 *     
 *     @Override
 *     public String getId() {
 *         return "database";
 *     }
 * }
 * </pre>
 *
 * @author Artaphy
 * @version 1.0
 * @since 1.0
 */
public interface IModule {
    /**
     * Called when the module is being enabled
     * @return true if enabled successfully
     */
    boolean onEnable();

    /**
     * Called when the module is being disabled
     */
    void onDisable();

    /**
     * Get the unique identifier of this module
     * @return Module identifier
     */
    String getId();

    /**
     * Get module description
     * @return Module description
     */
    String getDescription();

    /**
     * Check if module is enabled
     * @return true if enabled
     */
    boolean isEnabled();

    /**
     * Get module version
     * @return Module version string
     */
    String getVersion();

    /**
     * Get required dependencies for this module
     * @return Array of module IDs that this module depends on
     */
    String[] getDependencies();
} 