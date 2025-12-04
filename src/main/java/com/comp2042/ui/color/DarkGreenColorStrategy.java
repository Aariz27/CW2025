package com.comp2042.ui.color;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Strategy for dark green color (code 3).
 * Implements ColorStrategy interface following Strategy pattern.
 */
public class DarkGreenColorStrategy implements ColorStrategy {

    /** Creates a DarkGreen color strategy. */
    public DarkGreenColorStrategy() { }

    @Override
    public Paint getColor() {
        return Color.DARKGREEN;
    }
    
    @Override
    public int getColorCode() {
        return 3;
    }
}
