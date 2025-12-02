package com.comp2042.game.data;

import com.comp2042.game.operations.MatrixOperations;

/**
 * Immutable data class representing the result of clearing complete rows.
 * Contains the number of lines cleared, the updated board matrix, and score bonus.
 * 
 * <p>This class follows the Value Object pattern - it's immutable and
 * provides defensive copies of mutable data.
 */
public final class ClearRow {

    private final int linesRemoved;
    private final int[][] newMatrix;
    private final int scoreBonus;

    /**
     * Creates new clear row result data.
     * 
     * @param linesRemoved number of complete rows that were cleared
     * @param newMatrix the board matrix after clearing rows
     * @param scoreBonus score points awarded for clearing rows
     */
    public ClearRow(int linesRemoved, int[][] newMatrix, int scoreBonus) {
        this.linesRemoved = linesRemoved;
        this.newMatrix = newMatrix;
        this.scoreBonus = scoreBonus;
    }

    /**
     * Returns the number of rows that were cleared.
     * 
     * @return count of cleared lines
     */
    public int getLinesRemoved() {
        return linesRemoved;
    }

    /**
     * Returns a defensive copy of the board matrix after clearing rows.
     * 
     * @return 2D array representing the updated board
     */
    public int[][] getNewMatrix() {
        return MatrixOperations.copy(newMatrix);
    }

    /**
     * Returns the score bonus awarded for clearing rows.
     * Score is calculated as 50 * (lines cleared)².
     * 
     * @return score bonus points
     */
    public int getScoreBonus() {
        return scoreBonus;
    }
}
