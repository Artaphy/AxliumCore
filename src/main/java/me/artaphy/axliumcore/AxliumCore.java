package me.artaphy.axliumcore;

import me.artaphy.axliumcore.module.ModuleManager;
import me.artaphy.axliumcore.utils.Logger;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Core plugin class providing modular functionality framework
 * <p>
 * This plugin provides:
 * <ul>
 *     <li>Module management system</li>
 *     <li>Configuration framework</li>
 *     <li>Performance monitoring</li>
 *     <li>Utility services</li>
 * </ul>
 * 
 * Core features:
 * <ul>
 *     <li>Hot-reloadable modules</li>
 *     <li>Advanced configuration validation</li>
 *     <li>Performance optimization tools</li>
 *     <li>Comprehensive developer API</li>
 * </ul>
 *
 * @author Artaphy
 * @version 1.0
 * @since 1.0
 */
public final class AxliumCore extends JavaPlugin {
    
    private static AxliumCore instance;
    private Logger logger;
    private ModuleManager moduleManager;
    
    @Override
    public void onEnable() {
        instance = this;
        this.logger = new Logger(this);
        this.moduleManager = new ModuleManager(this);
        
        // Initialize core systems
        initializeCore();
        
        logger.info("AxliumCore has been enabled!");
    }

    @Override
    public void onDisable() {
        // Disable all modules
        if (moduleManager != null) {
            moduleManager.getModules().forEach(module -> 
                moduleManager.disableModule(module.getId()));
        }
        
        logger.info("AxliumCore has been disabled!");
    }

    /**
     * Initialize core systems and modules
     */
    private void initializeCore() {
        // Register core modules
        // TODO: Register your core modules here
    }

    public static AxliumCore getInstance() {
        return instance;
    }

    @Override
    public Logger getLogger() {
        return this.logger;
    }

    /**
     * Get the module manager instance
     * @return ModuleManager instance
     */
    public ModuleManager getModuleManager() {
        return moduleManager;
    }
}
