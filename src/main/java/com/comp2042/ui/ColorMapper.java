package com.comp2042.ui;

import com.comp2042.ui.color.ColorStrategyRegistry;
import javafx.scene.paint.Paint;

/**
 * Maps color codes to Paint objects.
 * Uses Strategy pattern instead of switch statements - new colors can be added
 * by creating new ColorStrategy implementations without modifying this class.
 */
public final class ColorMapper {
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
}