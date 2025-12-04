package com.comp2042.game.board;

import com.comp2042.game.data.ViewData;

/**
 * Interface for brick spawning operations.
 * Segregated interface following Interface Segregation Principle.
 */
public interface SpawnableBoard {
    /**
     * Attempts to spawn a new brick at the default position.
     * @return true if collision occurs (game over), false if successful
     */
    boolean trySpawnNewBrick();
    
    /**
     * Resets the board for a new game.
     * Clears the board matrix and resets the game state.
     */
    void newGame();

    /**
     * Holds the current brick or swaps with the held brick.
     * If no brick is held, stores the current brick and spawns a new one.
     * If a brick is already held, swaps the current brick with the held brick.
     * Can only be called when the current brick has not yet been merged to the background.
     *
     * @return ViewData containing updated brick information after the hold operation
     */
    ViewData holdBrick();
}

