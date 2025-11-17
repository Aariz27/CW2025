package com.comp2042.ui.color;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Strategy for yellow color (code 4).
 * Implements ColorStrategy interface following Strategy pattern.
 */
public class YellowColorStrategy implements ColorStrategy {
    @Override
    public Paint getColor() {
        return Color.YELLOW;
    }
    
    @Override
    public int getColorCode() {
        return 4;
    }
}

