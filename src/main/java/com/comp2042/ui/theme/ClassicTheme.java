package com.comp2042.ui.theme;

import com.comp2042.ui.color.ColorStrategyRegistry;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * The classic theme using the original game colors and style.
 * Delegates color lookup to the legacy ColorStrategyRegistry.
 */
public class ClassicTheme implements Theme {

    /** Creates the Classic theme. */
    public ClassicTheme() { }

    @Override
    public String getName() {
        return "Classic";
    }

    @Override
    public String getStylesheet() {
        return "window_style.css";
    }

    @Override
    public Paint getBrickColor(int brickCode) {
        return ColorStrategyRegistry.getColor(brickCode);
    }

    @Override
    public Paint getBoardBackgroundColor() {
        return Color.TRANSPARENT; // Classic uses an image background set in CSS
    }
}
