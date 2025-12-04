package com.comp2042.ui.color;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Strategy for red color (code 5).
 * Implements ColorStrategy interface following Strategy pattern.
 */
public class RedColorStrategy implements ColorStrategy {

    /** Creates a Red color strategy. */
    public RedColorStrategy() { }

    @Override
    public Paint getColor() {
        return Color.RED;
    }
    
    @Override
    public int getColorCode() {
        return 5;
    }
}
