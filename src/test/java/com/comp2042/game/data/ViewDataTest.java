package com.comp2042.game.data;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ViewData object.
 * Tests all view-related data including brick position, next brick preview, and ghost piece.
 */
class ViewDataTest {

    @Test
    void testConstructorAndGetters() {
        // Arrange
        int[][] brickData = {{1, 1}, {1, 0}};
        int xPosition = 5;
        int yPosition = 10;
        int[][] nextBrickData = {{1, 1, 1, 1}};
        int ghostX = 5;
        int ghostY = 18;

        // Act
        ViewData viewData = new ViewData(brickData, xPosition, yPosition, nextBrickData, ghostX, ghostY);

        // Assert
        assertArrayEquals(brickData, viewData.getBrickData());
        assertEquals(xPosition, viewData.getxPosition());
        assertEquals(yPosition, viewData.getyPosition());
        assertArrayEquals(nextBrickData, viewData.getNextBrickData());
        assertEquals(ghostX, viewData.getGhostX());
        assertEquals(ghostY, viewData.getGhostY());
    }

    @Test
    void testGetBrickDataReturnsDefensiveCopy() {
        // Arrange
        int[][] originalBrick = {{1, 1}, {1, 1}};
        ViewData viewData = new ViewData(originalBrick, 0, 0, new int[][]{{1}}, 0, 0);

        // Act
        int[][] returnedBrick = viewData.getBrickData();
        returnedBrick[0][0] = 999;

        // Assert - original should not be affected
        int[][] brickAgain = viewData.getBrickData();
        assertEquals(1, brickAgain[0][0], "Original brick data should not be modified");
        assertNotSame(returnedBrick, brickAgain, "Should return new copy each time");
    }

    @Test
    void testGetNextBrickDataReturnsDefensiveCopy() {
        // Arrange
        int[][] originalNext = {{1, 1, 1}};
        ViewData viewData = new ViewData(new int[][]{{1}}, 0, 0, originalNext, 0, 0);

        // Act
        int[][] returnedNext = viewData.getNextBrickData();
        returnedNext[0][0] = 999;

        // Assert - original should not be affected
        int[][] nextAgain = viewData.getNextBrickData();
        assertEquals(1, nextAgain[0][0], "Original next brick data should not be modified");
        assertNotSame(returnedNext, nextAgain, "Should return new copy each time");
    }

    @Test
    void testGhostPositionBelowCurrentPosition() {
        // Arrange - ghost should always be at or below current brick
        int[][] brick = {{1, 1}};
        int[][] next = {{1}};
        int currentY = 5;
        int ghostY = 15;

        // Act
        ViewData viewData = new ViewData(brick, 4, currentY, next, 4, ghostY);

        // Assert
        assertTrue(viewData.getGhostY() >= viewData.getyPosition(), 
                   "Ghost Y should be at or below current Y position");
    }

    @Test
    void testGhostAtSameXPosition() {
        // Arrange - ghost should typically have same X as current brick
        int[][] brick = {{1, 1, 1}};
        int[][] next = {{1}};
        int xPos = 3;

        // Act
        ViewData viewData = new ViewData(brick, xPos, 5, next, xPos, 15);

        // Assert
        assertEquals(viewData.getxPosition(), viewData.getGhostX(), 
                     "Ghost X should match current X position");
    }

    @Test
    void testSpawnPosition() {
        // Arrange - brick at spawn position (top of board)
        int[][] brick = {{1, 1}, {1, 1}};
        int[][] next = {{1, 1, 1, 1}};

        // Act
        ViewData viewData = new ViewData(brick, 4, 0, next, 4, 18);

        // Assert
        assertEquals(0, viewData.getyPosition(), "Brick should start at top");
        assertEquals(18, viewData.getGhostY(), "Ghost should be at bottom");
    }

    @Test
    void testDifferentBrickShapes() {
        // Test with I brick (1x4)
        int[][] iBrick = {{1, 1, 1, 1}};
        int[][] oNext = {{1, 1}, {1, 1}};
        ViewData iView = new ViewData(iBrick, 3, 5, oNext, 3, 19);
        
        assertEquals(1, iView.getBrickData().length);
        assertEquals(4, iView.getBrickData()[0].length);

        // Test with O brick (2x2)
        int[][] oBrick = {{1, 1}, {1, 1}};
        int[][] tNext = {{1, 1, 1}, {0, 1, 0}};
        ViewData oView = new ViewData(oBrick, 4, 10, tNext, 4, 17);
        
        assertEquals(2, oView.getBrickData().length);
        assertEquals(2, oView.getBrickData()[0].length);
    }

    @Test
    void testBoundaryPositions() {
        // Test brick at left edge
        int[][] brick = {{1, 1}};
        int[][] next = {{1}};
        ViewData leftEdge = new ViewData(brick, 0, 10, next, 0, 15);
        assertEquals(0, leftEdge.getxPosition());

        // Test brick at right edge (assuming 10-wide board)
        ViewData rightEdge = new ViewData(brick, 8, 10, next, 8, 15);
        assertEquals(8, rightEdge.getxPosition());
    }

    @Test
    void testGhostAtLandingPosition() {
        // Arrange - brick very close to landing
        int[][] brick = {{1, 1}};
        int[][] next = {{1}};
        int yPos = 17;
        int ghostY = 18;

        // Act
        ViewData viewData = new ViewData(brick, 4, yPos, next, 4, ghostY);

        // Assert
        assertEquals(1, viewData.getGhostY() - viewData.getyPosition(),
                     "Ghost should be just 1 cell below when about to land");
    }

    @Test
    void testComplexNextBrickShape() {
        // Arrange - current brick is O, next is complex T shape
        int[][] currentBrick = {{1, 1}, {1, 1}};
        int[][] nextTBrick = {{1, 1, 1}, {0, 1, 0}};

        // Act
        ViewData viewData = new ViewData(currentBrick, 4, 5, nextTBrick, 4, 15);

        // Assert
        assertEquals(2, viewData.getNextBrickData().length);
        assertEquals(3, viewData.getNextBrickData()[0].length);
    }
}

