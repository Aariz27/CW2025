package com.comp2042.game.board;

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
}

