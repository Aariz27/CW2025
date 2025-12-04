package com.comp2042.ui.theme;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * A retro theme inspired by classic handheld consoles.
 * Uses a monochrome green palette.
 */
public class RetroTheme implements Theme {

    private static final Color DARKEST_GREEN = Color.web("#0f380f");
    private static final Color DARK_GREEN = Color.web("#306230");
    private static final Color LIGHT_GREEN = Color.web("#8bac0f");
    private static final Color LIGHTEST_GREEN = Color.web("#9bbc0f");

    /** Creates the Retro theme. */
    public RetroTheme() { }

    @Override
    public String getName() {
        return "Retro";
    }

    @Override
    public String getStylesheet() {
        return "retro_style.css";
    }

    @Override
    public Paint getBrickColor(int brickCode) {
        if (brickCode == 0) return Color.TRANSPARENT;
        
        // Use darker colors for bricks so they stand out against the light background
        return switch (brickCode % 2) {
            case 0 -> DARKEST_GREEN;
            case 1 -> DARK_GREEN;
            default -> DARK_GREEN;
        };
    }

    @Override
    public Paint getBoardBackgroundColor() {
        return LIGHTEST_GREEN;
    }
}
