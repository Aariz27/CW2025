package com.comp2042.ui.color;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Strategy for aqua color (code 1).
 * Implements ColorStrategy interface following Strategy pattern.
 */
public class AquaColorStrategy implements ColorStrategy {
    @Override
    public Paint getColor() {
        return Color.AQUA;
    }
    
    @Override
    public int getColorCode() {
        return 1;
    }
}

