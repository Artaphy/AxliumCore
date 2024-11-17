package me.artaphy.axliumcore.utils;

import me.artaphy.axliumcore.AxliumCore;
import org.bukkit.plugin.Plugin;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Enhanced Logger that extends Java's Logger with additional features
 * <p>
 * This class provides enhanced logging capabilities with:
 * <ul>
 *     <li>Multiple log levels (INFO, WARN, ERROR, DEBUG)</li>
 *     <li>Plugin-specific prefixing</li>
 *     <li>Debug mode support</li>
 *     <li>Formatted message support</li>
 *     <li>Stack trace formatting</li>
 *     <li>Asynchronous logging</li>
 * </ul>
 *
 * @author Artaphy
 * @version 1.0
 * @since 1.0
 */
@SuppressWarnings("unused")
public class Logger extends java.util.logging.Logger {
    private final Plugin plugin;
    private boolean debugMode;
    private String prefix = "";

    /**
     * Constructor for AxliumCore's logger
     */
    public Logger() {
        this(AxliumCore.getInstance());
    }

    /**
     * Constructor for addon plugins
     * @param plugin The plugin instance
     */
    public Logger(Plugin plugin) {
        super(plugin.getName(), null);
        this.plugin = plugin;
        this.debugMode = false;
        setParent(plugin.getServer().getLogger());
        setLevel(Level.ALL);
    }

    @Override
    public void log(LogRecord record) {
        record.setMessage(formatMessage(record.getMessage()));
        super.log(record);
    }

    /**
     * Send a DEBUG level message (only if debug mode is enabled)
     * @param message The message to log
     */
    public void debug(String message) {
        if (debugMode) {
            log(Level.INFO, "[DEBUG] " + message);
        }
    }

    /**
     * Send a message with stack trace
     * @param message The message to log
     * @param throwable The throwable to get stack trace from
     */
    public void error(String message, Throwable throwable) {
        severe(message);
        
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        
        for (String line : sw.toString().split("\n")) {
            severe("    " + line.trim());
        }
    }

    /**
     * Send a formatted message
     * @param level The log level
     * @param message The message format
     * @param args The format arguments
     */
    public void logf(Level level, String message, Object... args) {
        log(level, String.format(message, args));
    }

    /**
     * Send multiple messages at INFO level
     * @param messages The messages to log
     */
    public void info(Collection<String> messages) {
        messages.forEach(this::info);
    }

    /**
     * Send a message asynchronously
     * @param level The log level
     * @param message The message to log
     */
    public void asyncLog(Level level, String message) {
        CompletableFuture.runAsync(() -> log(level, message));
    }

    /**
     * Enable or disable debug mode
     * @param enabled True to enable debug mode
     */
    public void setDebugMode(boolean enabled) {
        this.debugMode = enabled;
        info("Debug mode " + (enabled ? "enabled" : "disabled"));
    }

    /**
     * Set the log prefix
     * @param prefix The log prefix
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * Format a message with the plugin name and prefix
     * @param message The message to format
     * @return The formatted message
     */
    private String formatMessage(String message) {
        return String.format("[%s]%s %s", 
            plugin.getName(),
            prefix.isEmpty() ? "" : " " + prefix,
            message);
    }
} 