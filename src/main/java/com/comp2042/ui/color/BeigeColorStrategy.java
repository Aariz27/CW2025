package com.comp2042.ui.color;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Strategy for beige color (code 6).
 * Implements ColorStrategy interface following Strategy pattern.
 */
public class BeigeColorStrategy implements ColorStrategy {

    /** Creates a Beige color strategy. */
    public BeigeColorStrategy() { }

    @Override
    public Paint getColor() {
        return Color.BEIGE;
    }
    
    @Override
    public int getColorCode() {
        return 6;
    }
}
