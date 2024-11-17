package me.artaphy.axliumcore;

import me.artaphy.axliumcore.utils.Logger;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main class for AxliumCore plugin
 * 
 * @author Artaphy
 * @version 1.0
 * @since 1.0
 */
public final class AxliumCore extends JavaPlugin {
    
    private static AxliumCore instance;
    private Logger logger;
    
    @Override
    public void onEnable() {
        instance = this;
        this.logger = new Logger(this);
        logger.info("AxliumCore has been enabled!");
    }

    @Override
    public void onDisable() {
        logger.info("AxliumCore has been disabled!");
    }

    public static AxliumCore getInstance() {
        return instance;
    }

    @Override
    public Logger getLogger() {
        return this.logger;
    }
}
