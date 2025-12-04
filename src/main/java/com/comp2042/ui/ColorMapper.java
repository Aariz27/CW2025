package com.comp2042.ui;

import com.comp2042.ui.color.ColorStrategyRegistry;
import com.comp2042.ui.theme.Theme;
import com.comp2042.ui.theme.ThemeManager;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Maps color codes to Paint objects.
 * Uses Strategy pattern instead of switch statements - new colors can be added
 * by creating new ColorStrategy implementations without modifying this class.
 */
public final class ColorMapper {
    // Increase ghost opacity by 50% for better visibility over bright backgrounds
    private static final double GHOST_OPACITY = 0.75;
    
    private ColorMapper() {}
    
    /**
     * Gets the Paint color for the given color code.
     * Delegates to the active Theme via ThemeManager.
     * 
     * @param code the color code (0-7 for specific colors, any other value returns transparent)
     * @return the Paint color corresponding to the code
     */
    public static Paint getFillColor(int code) {
        Theme theme = ThemeManager.getInstance().getCurrentTheme();
        if (theme != null) {
            return theme.getBrickColor(code);
        }
        // Fallback to legacy registry if no theme active (should not happen)
        return ColorStrategyRegistry.getColor(code);
    }
    
    /**
     * Gets a semi-transparent ghost version of the color for the given code.
     * This provides a consistent visual style for ghost pieces across the game.
     * 
     * @param code the color code (0-7 for specific colors, any other value returns transparent white)
     * @return a semi-transparent Paint color for ghost piece rendering
     */
    public static Paint getGhostFillColor(int code) {
        Paint solidColor = getFillColor(code);
        
        // Convert to semi-transparent Color
        if (solidColor instanceof Color) {
            Color color = (Color) solidColor;
            return new Color(color.getRed(), color.getGreen(), color.getBlue(), GHOST_OPACITY);
        }
        
        // Fallback for non-Color Paint objects
        return Color.TRANSPARENT;
    }
}
