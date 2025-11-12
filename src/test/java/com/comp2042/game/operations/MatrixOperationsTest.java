package com.comp2042.game.operations;

import com.comp2042.game.data.ClearRow;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MatrixOperationsTest {

    @Test
    void intersectReturnsFalseWhenBrickFitsInsideMatrix() {
        int[][] board = new int[4][4];
        int[][] brick = {
                {1, 0},
                {1, 0}
        };

        boolean result = MatrixOperations.intersect(board, brick, 1, 1);

        assertFalse(result);
    }

    @Test
    void intersectReturnsTrueWhenBrickWouldLeaveBoard() {
        int[][] board = new int[4][4];
        int[][] brick = {
                {1, 0},
                {1, 0}
        };

        boolean result = MatrixOperations.intersect(board, brick, 3, 3);

        assertTrue(result);
    }

    @Test
    void mergePlacesBrickValuesIntoMatrixCopy() {
        int[][] board = new int[4][4];
        int[][] brick = {
                {1, 0},
                {1, 1}
        };

        int[][] merged = MatrixOperations.merge(board, brick, 1, 1);

        assertEquals(1, merged[1][1]);
        assertEquals(1, merged[2][1]);
        assertEquals(1, merged[2][2]);
        assertEquals(0, board[1][1], "original matrix should remain unchanged");
    }

    @Test
    void checkRemovingClearsFullRowsAndCalculatesBonus() {
        int[][] matrix = {
                {1, 1, 1},
                {0, 1, 0},
                {1, 1, 0}
        };

        ClearRow clearRow = MatrixOperations.checkRemoving(matrix);

        assertEquals(1, clearRow.getLinesRemoved());
        assertEquals(50, clearRow.getScoreBonus());
        int[][] newMatrix = clearRow.getNewMatrix();
        assertArrayEquals(new int[]{0, 1, 0}, newMatrix[1]);
        assertArrayEquals(new int[]{1, 1, 0}, newMatrix[2]);
    }
}

