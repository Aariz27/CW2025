package com.comp2042.game.board;

import com.comp2042.game.data.ViewData;

/**
 * Interface for board movement operations.
 * Segregated from Board interface - clients that only need movement
 * don't depend on clearing or spawning methods (Interface Segregation Principle).
 */
public interface MovableBoard {
    /**
     * Moves the current brick down one position.
     * @return true if the move was successful, false if blocked
     */
    boolean moveBrickDown();
    
    /**
     * Moves the current brick left one position.
     * @return true if the move was successful, false if blocked
     */
    boolean moveBrickLeft();
    
    /**
     * Moves the current brick right one position.
     * @return true if the move was successful, false if blocked
     */
    boolean moveBrickRight();
    
    /**
     * Rotates the current brick left (counter-clockwise).
     * @return true if the rotation was successful, false if blocked
     */
    boolean rotateLeftBrick();
    
    /**
     * Gets the current view data including brick position and shape.
     * @return the ViewData containing current game state
     */
    ViewData getViewData();
}

