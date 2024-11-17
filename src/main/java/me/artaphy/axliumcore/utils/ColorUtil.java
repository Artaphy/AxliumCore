package me.artaphy.axliumcore.utils;

import net.md_5.bungee.api.ChatColor;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Utility class for handling color codes and gradients in text
 * <p>
 * This class provides methods to:
 * <ul>
 *     <li>Process standard color codes (using & or §)</li>
 *     <li>Process HEX color codes (format: &{#RRGGBB})</li>
 *     <li>Create gradient effects (format: &{gradient:#RRGGBB:#RRGGBB})</li>
 *     <li>Create rainbow effects (format: &{rainbow#speed:saturation:brightness})</li>
 * </ul>
 * 
 * Performance optimizations:
 * <ul>
 *     <li>Uses caching for frequently used color patterns</li>
 *     <li>Optimized regex patterns for color code matching</li>
 *     <li>Efficient string manipulation for color processing</li>
 * </ul>
 * 
 * @author Artaphy
 * @version 1.0
 * @since 1.0
 */
@SuppressWarnings("unused")
public class ColorUtil {
    // Regular expression patterns
    private static final Pattern HEX_PATTERN = Pattern.compile("&\\{#([A-Fa-f0-9]{6})}");
    private static final Pattern GRADIENT_PATTERN = Pattern.compile("&\\{(?<type>gradient|g)(#(?<speed>\\d+))?(?<hex>(:#([A-Fa-f\\d]{6}|[A-Fa-f\\d]{3})){2,})(:(?<loop>l|L|loop))?}");
    private static final Pattern RAINBOW_PATTERN = Pattern.compile("&\\{(?<type>rainbow|r)(#(?<speed>\\d+))?(:(?<saturation>\\d*\\.?\\d+))?(:(?<brightness>\\d*\\.?\\d+))?(:(?<loop>l|L|loop))?}");

    // Cache mechanism to avoid processing the same strings repeatedly
    private static final ConcurrentHashMap<String, String> COLOR_CACHE = new ConcurrentHashMap<>();
    private static final int CACHE_SIZE = 1000; // Maximum cache size
    
    /**
     * Format color codes in a string with performance monitoring
     * <p>
     * Supported formats:
     * <ul>
     *     <li>&a, &b, etc. - Standard color codes</li>
     *     <li>&{#RRGGBB} - HEX colors</li>
     *     <li>&{gradient:#RRGGBB:#RRGGBB} - Gradient colors</li>
     *     <li>&{rainbow#speed:sat:bright} - Rainbow effect</li>
     * </ul>
     *
     * @param text The text to format
     * @return Formatted text with colors
     * @throws IllegalArgumentException if the color pattern is invalid
     */
    public static String format(String text) {
        if (text == null || text.isEmpty()) return "";
        
        PerformanceMonitor.startTiming("ColorUtil.format");
        try {
            // Check cache first
            String cached = COLOR_CACHE.get(text);
            if (cached != null) return cached;
            
            // Process colors in sequence
            text = processHexColors(text);
            text = processGradients(text);
            text = processRainbow(text);
            
            // Process standard color codes
            String result = ChatColor.translateAlternateColorCodes('&', text);
            
            // Cache result if space available
            if (COLOR_CACHE.size() < CACHE_SIZE) {
                COLOR_CACHE.put(text, result);
            }
            
            return result;
        } finally {
            PerformanceMonitor.stopTiming("ColorUtil.format");
        }
    }

    /**
     * Process HEX color codes
     */
    private static String processHexColors(String text) {
        Matcher matcher = HEX_PATTERN.matcher(text);
        while (matcher.find()) {
            String color = matcher.group(1);
            text = text.replace(matcher.group(), ChatColor.of("#" + color).toString());
        }
        return text;
    }

    /**
     * Process gradient color patterns
     */
    private static String processGradients(String text) {
        Matcher matcher = GRADIENT_PATTERN.matcher(text);
        while (matcher.find()) {
            String speed = matcher.group("speed");
            String hexColors = matcher.group("hex");
            boolean loop = matcher.group("loop") != null;
            
            // Extract colors
            String[] colors = hexColors.substring(1).split(":");
            List<Color> colorList = new ArrayList<>();
            for (String color : colors) {
                if (color.startsWith("#")) color = color.substring(1);
                colorList.add(Color.decode("#" + color));
            }
            
            // Generate gradient text
            String content = text.substring(matcher.end(), findEndContent(text, matcher.end()));
            String gradient = createGradient(content, colorList, 
                    speed != null ? Integer.parseInt(speed) : 1, loop);
            
            text = text.replace(matcher.group() + content, gradient);
        }
        return text;
    }

    /**
     * Process rainbow color patterns
     */
    private static String processRainbow(String text) {
        Matcher matcher = RAINBOW_PATTERN.matcher(text);
        while (matcher.find()) {
            int speed = matcher.group("speed") != null ? 
                    Integer.parseInt(matcher.group("speed")) : 1;
            float saturation = matcher.group("saturation") != null ? 
                    Float.parseFloat(matcher.group("saturation")) : 1.0f;
            float brightness = matcher.group("brightness") != null ? 
                    Float.parseFloat(matcher.group("brightness")) : 1.0f;
            boolean loop = matcher.group("loop") != null;
            
            String content = text.substring(matcher.end(), findEndContent(text, matcher.end()));
            String rainbow = createRainbow(content, speed, saturation, brightness, loop);
            
            text = text.replace(matcher.group() + content, rainbow);
        }
        return text;
    }

    /**
     * Create gradient text
     */
    private static String createGradient(String text, List<Color> colors, int speed, boolean loop) {
        StringBuilder result = new StringBuilder();
        double step = 1.0 / (text.length() - 1);
        
        for (int i = 0; i < text.length(); i++) {
            double percent = i * step;
            if (loop) percent = (percent + (System.currentTimeMillis() / (1000.0 / speed))) % 1.0;
            
            Color color = interpolateColors(colors, percent);
            result.append(ChatColor.of(color)).append(text.charAt(i));
        }
        
        return result.toString();
    }

    /**
     * Create rainbow text
     */
    private static String createRainbow(String text, int speed, float saturation, float brightness, boolean loop) {
        StringBuilder result = new StringBuilder();
        double step = 1.0 / (text.length() - 1);
        
        for (int i = 0; i < text.length(); i++) {
            double percent = i * step;
            if (loop) percent = (percent + (System.currentTimeMillis() / (1000.0 / speed))) % 1.0;
            
            Color color = Color.getHSBColor((float)percent, saturation, brightness);
            result.append(ChatColor.of(color)).append(text.charAt(i));
        }
        
        return result.toString();
    }

    /**
     * Interpolate between multiple colors
     */
    private static Color interpolateColors(List<Color> colors, double percent) {
        int colorCount = colors.size();
        double percentPerColor = 1.0 / (colorCount - 1);
        int colorIndex = (int)(percent / percentPerColor);
        
        if (colorIndex >= colorCount - 1) return colors.get(colorCount - 1);
        
        double localPercent = (percent - (colorIndex * percentPerColor)) / percentPerColor;
        Color color1 = colors.get(colorIndex);
        Color color2 = colors.get(colorIndex + 1);
        
        return interpolateColor(color1, color2, localPercent);
    }

    /**
     * Interpolate between two colors
     */
    private static Color interpolateColor(Color color1, Color color2, double percent) {
        int red = (int)(color1.getRed() + percent * (color2.getRed() - color1.getRed()));
        int green = (int)(color1.getGreen() + percent * (color2.getGreen() - color1.getGreen()));
        int blue = (int)(color1.getBlue() + percent * (color2.getBlue() - color1.getBlue()));
        return new Color(red, green, blue);
    }

    /**
     * Find the end of the content for gradient/rainbow processing
     */
    private static int findEndContent(String text, int start) {
        int depth = 1;
        for (int i = start; i < text.length(); i++) {
            if (text.charAt(i) == '{') depth++;
            else if (text.charAt(i) == '}') {
                depth--;
                if (depth == 0) return i;
            }
        }
        return text.length();
    }

    /**
     * Format color codes in a list of strings
     * @param texts The list of texts to format
     * @return Formatted texts with colors
     */
    public static List<String> format(List<String> texts) {
        return texts.stream()
                   .map(ColorUtil::format)
                   .collect(Collectors.toList());
    }

    /**
     * Strip color codes from a string
     * @param text The text to strip colors from
     * @return Text without colors
     */
    public static String stripColor(String text) {
        if (text == null) return null;
        return ChatColor.stripColor(text);
    }
} 