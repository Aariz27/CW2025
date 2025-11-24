package com.comp2042.ui.color;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Strategy for blue violet color (code 2).
 * Implements ColorStrategy interface following Strategy pattern.
 */
public class BlueVioletColorStrategy implements ColorStrategy {
    @Override
    public Paint getColor() {
        return Color.BLUEVIOLET;
    }
    
    @Override
    public int getColorCode() {
        return 2;
    }
}

