package com.comp2042.ui;

import com.comp2042.ui.color.ColorStrategyRegistry;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Maps color codes to Paint objects.
 * Uses Strategy pattern instead of switch statements - new colors can be added
 * by creating new ColorStrategy implementations without modifying this class.
 */
public final class ColorMapper {
    private static final double GHOST_OPACITY = 0.3;
    
    private ColorMapper() {}
    
    /**
     * Gets the Paint color for the given color code.
     * Delegates to registry which uses polymorphism to select the correct strategy.
     * 
     * @param code the color code (0-7 for specific colors, any other value returns white)
     * @return the Paint color corresponding to the code
     */
    public static Paint getFillColor(int code) {
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
        Paint solidColor = ColorStrategyRegistry.getColor(code);
        
        // Convert to semi-transparent Color
        if (solidColor instanceof Color) {
            Color color = (Color) solidColor;
            return new Color(color.getRed(), color.getGreen(), color.getBlue(), GHOST_OPACITY);
        }
        
        // Fallback for non-Color Paint objects
        return Color.TRANSPARENT;
    }
}