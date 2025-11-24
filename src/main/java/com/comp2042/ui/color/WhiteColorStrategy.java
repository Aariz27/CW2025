package com.comp2042.ui.color;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Strategy for white color (default/fallback).
 * Implements ColorStrategy interface following Strategy pattern.
 * Used as default strategy when color code is not recognized.
 */
public class WhiteColorStrategy implements ColorStrategy {
    @Override
    public Paint getColor() {
        return Color.WHITE;
    }
    
    @Override
    public int getColorCode() {
        return -1; // Default/fallback code
    }
}

