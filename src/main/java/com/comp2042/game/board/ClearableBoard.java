package com.comp2042.game.board;

import com.comp2042.game.data.ClearRow;

/**
 * Interface for row clearing operations.
 * Segregated interface following Interface Segregation Principle.
 */
public interface ClearableBoard {
    /**
     * Merges the current brick into the background board matrix.
     * This is called when a brick can no longer move down.
     */
    void mergeBrickToBackground();
    
    /**
     * Checks for and clears any complete rows.
     * @return ClearRow containing information about cleared rows and score bonus
     */
    ClearRow clearRows();
    
    /**
     * Gets the current board matrix state.
     * @return 2D array representing the board state
     */
    int[][] getBoardMatrix();
}

