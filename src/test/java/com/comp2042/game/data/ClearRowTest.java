package com.comp2042.game.data;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ClearRow data object.
 * Tests data integrity, getters, and defensive copying of matrix.
 */
class ClearRowTest {

    @Test
    void testConstructorAndGetters() {
        // Arrange
        int[][] matrix = {{1, 2}, {3, 4}};
        int linesRemoved = 2;
        int scoreBonus = 100;

        // Act
        ClearRow clearRow = new ClearRow(linesRemoved, matrix, scoreBonus);

        // Assert
        assertEquals(linesRemoved, clearRow.getLinesRemoved());
        assertEquals(scoreBonus, clearRow.getScoreBonus());
        assertArrayEquals(matrix, clearRow.getNewMatrix());
    }

    @Test
    void testGetNewMatrixReturnsDefensiveCopy() {
        // Arrange
        int[][] originalMatrix = {{1, 2}, {3, 4}};
        ClearRow clearRow = new ClearRow(1, originalMatrix, 50);

        // Act
        int[][] returnedMatrix = clearRow.getNewMatrix();
        returnedMatrix[0][0] = 999; // Modify the returned matrix

        // Assert - original should not be affected
        int[][] matrixAgain = clearRow.getNewMatrix();
        assertEquals(1, matrixAgain[0][0], "Original matrix should not be modified");
        assertNotSame(returnedMatrix, matrixAgain, "Should return new copy each time");
    }

    @Test
    void testZeroLinesRemoved() {
        // Arrange
        int[][] matrix = {{1, 2}, {3, 4}};

        // Act
        ClearRow clearRow = new ClearRow(0, matrix, 0);

        // Assert
        assertEquals(0, clearRow.getLinesRemoved());
        assertEquals(0, clearRow.getScoreBonus());
    }

    @Test
    void testMultipleLinesRemoved() {
        // Arrange
        int[][] matrix = {{1, 2, 3}, {4, 5, 6}};
        int linesRemoved = 4; // Tetris!

        // Act
        ClearRow clearRow = new ClearRow(linesRemoved, matrix, 800);

        // Assert
        assertEquals(4, clearRow.getLinesRemoved());
        assertEquals(800, clearRow.getScoreBonus());
    }

    @Test
    void testEmptyMatrix() {
        // Arrange
        int[][] emptyMatrix = new int[0][0];

        // Act
        ClearRow clearRow = new ClearRow(0, emptyMatrix, 0);

        // Assert
        assertEquals(0, clearRow.getLinesRemoved());
        assertArrayEquals(emptyMatrix, clearRow.getNewMatrix());
    }

    @Test
    void testLargeScoreBonus() {
        // Arrange
        int[][] matrix = {{1}};
        int largeScore = 99999;

        // Act
        ClearRow clearRow = new ClearRow(1, matrix, largeScore);

        // Assert
        assertEquals(largeScore, clearRow.getScoreBonus());
    }
}

