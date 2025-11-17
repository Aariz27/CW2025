package com.comp2042.ui.color;

import javafx.scene.paint.Paint;
import java.util.HashMap;
import java.util.Map;

/**
 * Registry for color strategies.
 * Centralizes strategy lookup - new strategies can be registered without
 * modifying existing code (Open/Closed Principle).
 */
public class ColorStrategyRegistry {
    private static final Map<Integer, ColorStrategy> strategies = new HashMap<>();
    
    // Register all available color strategies at initialization
    static {
        register(new TransparentColorStrategy());
        register(new AquaColorStrategy());
        register(new BlueVioletColorStrategy());
        register(new DarkGreenColorStrategy());
        register(new YellowColorStrategy());
        register(new RedColorStrategy());
        register(new BeigeColorStrategy());
        register(new BurlyWoodColorStrategy());
    }
    
    private static void register(ColorStrategy strategy) {
        strategies.put(strategy.getColorCode(), strategy);
    }
    
    /**
     * Gets the color using polymorphic strategy selection.
     * Different strategies are selected based on code, demonstrating polymorphism.
     * 
     * @param code the color code
     * @return the Paint color, or white as default if code not found
     */
    public static Paint getColor(int code) {
        ColorStrategy strategy = strategies.getOrDefault(code, new WhiteColorStrategy());
        return strategy.getColor();
    }
}

