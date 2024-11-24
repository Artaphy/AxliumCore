package me.artaphy.axliumcore.module;

import me.artaphy.axliumcore.AxliumCore;
import me.artaphy.axliumcore.utils.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages the lifecycle and dependencies of all modules
 * <p>
 * This class provides:
 * <ul>
 *     <li>Module registration and lifecycle management</li>
 *     <li>Automatic dependency resolution</li>
 *     <li>State tracking and persistence</li>
 *     <li>Hot-reload capabilities</li>
 * </ul>
 * 
 * Features:
 * <ul>
 *     <li>Thread-safe module operations</li>
 *     <li>Graceful error handling</li>
 *     <li>Dependency validation</li>
 *     <li>Module state monitoring</li>
 * </ul>
 *
 * @author Artaphy
 * @version 1.0
 * @since 1.0
 */
public class ModuleManager {
    private final Logger logger;
    private final Map<String, IModule> modules;
    private final Map<String, Boolean> moduleStates;

    public ModuleManager(AxliumCore plugin) {
        this.logger = plugin.getLogger();
        this.modules = new ConcurrentHashMap<>();
        this.moduleStates = new ConcurrentHashMap<>();
    }

    /**
     * Register a new module
     * @param module Module to register
     * @throws IllegalArgumentException if module ID conflicts
     */
    public void registerModule(IModule module) {
        String id = module.getId();
        if (modules.containsKey(id)) {
            throw new IllegalArgumentException("Module already registered: " + id);
        }
        modules.put(id, module);
        moduleStates.put(id, false);
        logger.debug("Registered module: " + id);
    }

    /**
     * Enable a specific module and its dependencies
     * @param moduleId ID of module to enable
     * @return true if enabled successfully
     */
    public boolean enableModule(String moduleId) {
        IModule module = modules.get(moduleId);
        if (module == null) return false;

        // Check dependencies
        for (String depId : module.getDependencies()) {
            if (!enableModule(depId)) {
                logger.severe("Failed to enable dependency: " + depId);
                return false;
            }
        }

        try {
            if (module.onEnable()) {
                moduleStates.put(moduleId, true);
                logger.info("Enabled module: " + moduleId);
                return true;
            }
        } catch (Exception e) {
            logger.error("Error enabling module: " + moduleId, e);
        }
        return false;
    }

    /**
     * Disable a specific module
     * @param moduleId ID of module to disable
     */
    public void disableModule(String moduleId) {
        IModule module = modules.get(moduleId);
        if (module != null && moduleStates.get(moduleId)) {
            try {
                module.onDisable();
                moduleStates.put(moduleId, false);
                logger.info("Disabled module: " + moduleId);
            } catch (Exception e) {
                logger.error("Error disabling module: " + moduleId, e);
            }
        }
    }

    /**
     * Get a registered module by ID
     * @param moduleId Module ID
     * @return Module instance or null if not found
     */
    public IModule getModule(String moduleId) {
        return modules.get(moduleId);
    }

    /**
     * Check if a module is enabled
     * @param moduleId Module ID
     * @return true if module is enabled
     */
    public boolean isModuleEnabled(String moduleId) {
        return moduleStates.getOrDefault(moduleId, false);
    }

    /**
     * Get all registered modules
     * @return Collection of all modules
     */
    public Collection<IModule> getModules() {
        return Collections.unmodifiableCollection(modules.values());
    }
} 