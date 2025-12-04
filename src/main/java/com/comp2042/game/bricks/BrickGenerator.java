package com.comp2042.game.bricks;

/**
 * Interface for generating bricks in the game.
 * Implementations provide strategies for brick generation (e.g., random, predefined sequences).
 * 
 * <p>This interface follows the Strategy pattern, allowing different brick generation
 * algorithms to be swapped easily.
 */
public interface BrickGenerator {

    /**
     * Returns the next brick to be used in the game.
     * This method typically advances the internal state.
     * 
     * @return the next brick
     */
    Brick getBrick();

    /**
     * Returns a preview of the next brick without advancing the state.
     * Used for displaying the "next brick" preview in the UI.
     * 
     * @return the next brick (peek)
     */
    Brick getNextBrick();
}
