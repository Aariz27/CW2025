package com.comp2042.ui.color;

import javafx.scene.paint.Paint;

/**
 * Strategy interface for color mapping.
 * Each implementation encapsulates a specific color, allowing polymorphic
 * color selection without conditional statements.
 */
public interface ColorStrategy {
    Paint getColor();
    int getColorCode();
}

