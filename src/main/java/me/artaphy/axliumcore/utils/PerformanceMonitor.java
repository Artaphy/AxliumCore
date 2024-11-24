package me.artaphy.axliumcore.utils;

import me.artaphy.axliumcore.AxliumCore;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;

/**
 * Performance monitoring and optimization utility
 * <p>
 * This class provides methods to:
 * <ul>
 *     <li>Track method execution times</li>
 *     <li>Monitor memory usage</li>
 *     <li>Detect performance bottlenecks</li>
 *     <li>Generate performance reports</li>
 * </ul>
 * 
 * Monitoring features:
 * <ul>
 *     <li>Execution time tracking</li>
 *     <li>Memory usage analysis</li>
 *     <li>Automatic warning triggers</li>
 *     <li>Performance statistics</li>
 * </ul>
 * 
 * Usage example:
 * <pre>
 * PerformanceMonitor.startTiming("database.query");
 * // ... perform database operation ...
 * PerformanceMonitor.stopTiming("database.query");
 * </pre>
 *
 * @author Artaphy
 * @version 1.0
 * @since 1.0
 */
public class PerformanceMonitor {
    private static final Map<String, AtomicLong> EXECUTION_TIMES = new ConcurrentHashMap<>();
    private static final Map<String, AtomicLong> EXECUTION_COUNTS = new ConcurrentHashMap<>();
    private static final Map<String, Long> START_TIMES = new ConcurrentHashMap<>();
    private static final Logger logger = AxliumCore.getInstance().getLogger();
    
    private static final long SLOW_EXECUTION_THRESHOLD = 50; // milliseconds
    private static final long MEMORY_WARNING_THRESHOLD = 85; // percentage
    private static final long MONITOR_INTERVAL = 300L; // 15 seconds (in ticks)
    
    static {
        startMemoryMonitor();
    }
    
    /**
     * Start timing an operation
     * 
     * @param operationName The unique identifier for the operation
     * @throws IllegalStateException if timing has already started for this operation
     */
    public static void startTiming(String operationName) {
        if (START_TIMES.containsKey(operationName)) {
            throw new IllegalStateException("Timing already started for: " + operationName);
        }
        START_TIMES.put(operationName, System.nanoTime());
    }
    
    /**
     * Stop timing an operation and record its execution time
     * Logs a warning if execution time exceeds threshold
     * 
     * @param operationName The unique identifier for the operation
     * @throws IllegalStateException if timing hasn't been started for this operation
     */
    public static void stopTiming(String operationName) {
        Long startTime = START_TIMES.remove(operationName);
        if (startTime == null) {
            throw new IllegalStateException("Timing not started for: " + operationName);
        }
        
        long executionTime = System.nanoTime() - startTime;
        long milliseconds = executionTime / 1_000_000;
        
        EXECUTION_TIMES.computeIfAbsent(operationName, k -> new AtomicLong())
                      .addAndGet(executionTime);
        EXECUTION_COUNTS.computeIfAbsent(operationName, k -> new AtomicLong())
                       .incrementAndGet();
        
        // Log warning if execution time exceeds threshold
        if (milliseconds > SLOW_EXECUTION_THRESHOLD) {
            logger.logf(Level.WARNING, 
                "Slow execution detected: %s took %d ms (threshold: %d ms)",
                operationName, milliseconds, SLOW_EXECUTION_THRESHOLD);
        }
    }
    
    /**
     * Start periodic memory monitoring
     */
    private static void startMemoryMonitor() {
        new BukkitRunnable() {
            @Override
            public void run() {
                checkMemoryUsage();
            }
        }.runTaskTimer(AxliumCore.getInstance(), MONITOR_INTERVAL, MONITOR_INTERVAL);
    }
    
    /**
     * Check current memory usage and log warning if it exceeds threshold
     */
    private static void checkMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory();
        long usedMemory = runtime.totalMemory() - runtime.freeMemory();
        
        double usedPercentage = (usedMemory * 100.0) / maxMemory;
        
        if (usedPercentage > MEMORY_WARNING_THRESHOLD) {
            logger.logf(Level.WARNING,
                "High memory usage detected: %.1f%% (%.2f MB / %.2f MB)",
                usedPercentage,
                usedMemory / 1024.0 / 1024.0,
                maxMemory / 1024.0 / 1024.0);
        }
    }
} 