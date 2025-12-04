package com.comp2042.ui.theme;

import javafx.scene.paint.Paint;

/**
 * Interface defining the visual appearance of the game.
 * Implementations provide color palettes for game elements and CSS stylesheets.
 * 
 * <p>This interface is part of the Strategy pattern used for theming.
 */
public interface Theme {
    
    /**
     * Gets the name of the theme.
     * 
     * @return the display name of the theme
     */
    String getName();

    /**
     * Gets the path to the CSS stylesheet for this theme.
     * 
     * @return the resource path to the CSS file
     */
    String getStylesheet();

    /**
     * Gets the color for a specific brick type.
     * 
     * @param brickCode the integer code representing the brick type (1-7)
     * @return the Paint object (Color or Gradient) for the brick
     */
    Paint getBrickColor(int brickCode);
    
    /**
     * Gets the background color for the game board.
     * 
     * @return the Paint object for the board background
     */
    Paint getBoardBackgroundColor();
}

