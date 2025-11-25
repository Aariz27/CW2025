package com.comp2042.game.data;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for DownData composite object.
 * Tests composition of ClearRow and ViewData.
 */
class DownDataTest {

    @Test
    void testConstructorAndGetters() {
        // Arrange
        int[][] matrix = {{1, 2}, {3, 4}};
        ClearRow clearRow = new ClearRow(2, matrix, 100);
        
        int[][] brickData = {{1, 1}, {1, 0}};
        int[][] nextBrickData = {{1, 1, 1, 1}};
        ViewData viewData = new ViewData(brickData, 5, 10, nextBrickData, 5, 15);

        // Act
        DownData downData = new DownData(clearRow, viewData);

        // Assert
        assertNotNull(downData.getClearRow());
        assertNotNull(downData.getViewData());
        assertEquals(2, downData.getClearRow().getLinesRemoved());
        assertEquals(5, downData.getViewData().getxPosition());
    }

    @Test
    void testWithNullClearRow() {
        // Arrange
        int[][] brickData = {{1, 1}};
        int[][] nextBrickData = {{1, 1}};
        ViewData viewData = new ViewData(brickData, 0, 0, nextBrickData, 0, 0);

        // Act
        DownData downData = new DownData(null, viewData);

        // Assert
        assertNull(downData.getClearRow());
        assertNotNull(downData.getViewData());
    }

    @Test
    void testWithNullViewData() {
        // Arrange
        int[][] matrix = {{1}};
        ClearRow clearRow = new ClearRow(1, matrix, 50);

        // Act
        DownData downData = new DownData(clearRow, null);

        // Assert
        assertNotNull(downData.getClearRow());
        assertNull(downData.getViewData());
    }

    @Test
    void testNoLinesCleared() {
        // Arrange
        int[][] matrix = {{0, 0}, {0, 0}};
        ClearRow clearRow = new ClearRow(0, matrix, 0);
        
        int[][] brickData = {{1, 1}};
        int[][] nextBrickData = {{1}};
        ViewData viewData = new ViewData(brickData, 3, 5, nextBrickData, 3, 10);

        // Act
        DownData downData = new DownData(clearRow, viewData);

        // Assert
        assertEquals(0, downData.getClearRow().getLinesRemoved());
        assertEquals(0, downData.getClearRow().getScoreBonus());
    }

    @Test
    void testMultipleLinesCleared() {
        // Arrange
        int[][] matrix = {{1, 1, 1}, {2, 2, 2}};
        ClearRow clearRow = new ClearRow(4, matrix, 800); // Tetris
        
        int[][] brickData = {{1, 1, 1, 1}};
        int[][] nextBrickData = {{1, 1}};
        ViewData viewData = new ViewData(brickData, 4, 0, nextBrickData, 4, 18);

        // Act
        DownData downData = new DownData(clearRow, viewData);

        // Assert
        assertEquals(4, downData.getClearRow().getLinesRemoved());
        assertEquals(800, downData.getClearRow().getScoreBonus());
        assertEquals(0, downData.getViewData().getyPosition());
    }
}

