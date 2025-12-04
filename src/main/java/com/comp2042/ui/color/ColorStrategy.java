package com.comp2042.ui.color;

import javafx.scene.paint.Paint;

/**
 * Strategy interface for color mapping.
 * Each implementation encapsulates a specific color, allowing polymorphic
 * color selection without conditional statements.
 */
public interface ColorStrategy {
    /**
     * Gets the paint value for this strategy.
     *
     * @return the paint associated with the color strategy
     */
    Paint getColor();

    /**
     * Gets the numeric code associated with this color.
     *
     * @return the color code used in the board matrix
     */
    int getColorCode();
}
