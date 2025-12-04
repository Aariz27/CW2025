package com.comp2042.ui.theme;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * A high-contrast neon theme with a dark background.
 * Uses bright, saturated colors for bricks.
 */
public class NeonTheme implements Theme {

    /** Creates the Neon theme. */
    public NeonTheme() { }

    @Override
    public String getName() {
        return "Neon";
    }

    @Override
    public String getStylesheet() {
        return "neon_style.css";
    }

    @Override
    public Paint getBrickColor(int brickCode) {
        return switch (brickCode) {
            case 1 -> Color.CYAN;
            case 2 -> Color.MAGENTA;
            case 3 -> Color.LIME;
            case 4 -> Color.YELLOW;
            case 5 -> Color.RED;
            case 6 -> Color.ORANGE;
            case 7 -> Color.PURPLE;
            default -> Color.TRANSPARENT; // Only empty cells (0) are transparent
        };
    }

    @Override
    public Paint getBoardBackgroundColor() {
        return Color.BLACK;
    }
}
