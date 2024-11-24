package me.artaphy.axliumcore.module;

import me.artaphy.axliumcore.AxliumCore;
import me.artaphy.axliumcore.utils.Logger;

/**
 * Base implementation for plugin modules
 * <p>
 * This class provides:
 * <ul>
 *     <li>Common module functionality</li>
 *     <li>Lifecycle management</li>
 *     <li>Resource handling</li>
 *     <li>Logging integration</li>
 * </ul>
 * 
 * Implementation guidelines:
 * <ul>
 *     <li>Override onEnable() for initialization</li>
 *     <li>Override onDisable() for cleanup</li>
 *     <li>Use provided logger for messages</li>
 *     <li>Implement getId() and getDescription()</li>
 * </ul>
 *
 * @author Artaphy
 * @version 1.0
 * @since 1.0
 */
public abstract class AbstractModule implements IModule {
    protected final AxliumCore plugin;
    protected final Logger logger;
    protected boolean enabled;
    protected final String version;
    
    protected AbstractModule(AxliumCore plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        this.enabled = false;
        this.version = "1.0";
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public String[] getDependencies() {
        return new String[0];
    }
} 