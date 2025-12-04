package com.comp2042.ui.color;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Strategy for burly wood color (code 7).
 * Implements ColorStrategy interface following Strategy pattern.
 */
public class BurlyWoodColorStrategy implements ColorStrategy {

    /** Creates a BurlyWood color strategy. */
    public BurlyWoodColorStrategy() { }

    @Override
    public Paint getColor() {
        return Color.BURLYWOOD;
    }
    
    @Override
    public int getColorCode() {
        return 7;
    }
}
