package com.comp2042.ui.color;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Strategy for transparent color (code 0).
 * Implements ColorStrategy interface following Strategy pattern.
 */
public class TransparentColorStrategy implements ColorStrategy {
    @Override
    public Paint getColor() {
        return Color.TRANSPARENT;
    }
    
    @Override
    public int getColorCode() {
        return 0;
    }
}

