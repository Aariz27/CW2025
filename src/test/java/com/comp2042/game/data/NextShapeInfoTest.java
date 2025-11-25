package com.comp2042.game.data;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for NextShapeInfo data object.
 * Tests rotation position tracking and defensive copying.
 */
class NextShapeInfoTest {

    @Test
    void testConstructorAndGetters() {
        // Arrange
        int[][] shape = {{1, 1}, {1, 1}};
        int position = 0;

        // Act
        NextShapeInfo info = new NextShapeInfo(shape, position);

        // Assert
        assertArrayEquals(shape, info.getShape());
        assertEquals(position, info.getPosition());
    }

    @Test
    void testGetShapeReturnsDefensiveCopy() {
        // Arrange
        int[][] originalShape = {{1, 1, 1}, {0, 1, 0}};
        NextShapeInfo info = new NextShapeInfo(originalShape, 1);

        // Act
        int[][] returnedShape = info.getShape();
        returnedShape[0][0] = 999; // Modify the returned shape

        // Assert - original should not be affected
        int[][] shapeAgain = info.getShape();
        assertEquals(1, shapeAgain[0][0], "Original shape should not be modified");
        assertNotSame(returnedShape, shapeAgain, "Should return new copy each time");
    }

    @Test
    void testDifferentRotationPositions() {
        // Arrange & Act
        NextShapeInfo pos0 = new NextShapeInfo(new int[][]{{1, 1}}, 0);
        NextShapeInfo pos1 = new NextShapeInfo(new int[][]{{1}, {1}}, 1);
        NextShapeInfo pos2 = new NextShapeInfo(new int[][]{{1, 1}}, 2);
        NextShapeInfo pos3 = new NextShapeInfo(new int[][]{{1}, {1}}, 3);

        // Assert
        assertEquals(0, pos0.getPosition());
        assertEquals(1, pos1.getPosition());
        assertEquals(2, pos2.getPosition());
        assertEquals(3, pos3.getPosition());
    }

    @Test
    void testIShape() {
        // Arrange - I brick horizontal
        int[][] iShape = {{1, 1, 1, 1}};
        int position = 0;

        // Act
        NextShapeInfo info = new NextShapeInfo(iShape, position);

        // Assert
        assertEquals(1, info.getShape().length);
        assertEquals(4, info.getShape()[0].length);
        assertEquals(0, info.getPosition());
    }

    @Test
    void testOShape() {
        // Arrange - O brick (square, no rotation)
        int[][] oShape = {{1, 1}, {1, 1}};
        int position = 0;

        // Act
        NextShapeInfo info = new NextShapeInfo(oShape, position);

        // Assert
        assertEquals(2, info.getShape().length);
        assertEquals(2, info.getShape()[0].length);
    }

    @Test
    void testComplexShape() {
        // Arrange - T brick in position 1
        int[][] tShape = {{0, 1}, {1, 1}, {0, 1}};
        int position = 1;

        // Act
        NextShapeInfo info = new NextShapeInfo(tShape, position);

        // Assert
        assertEquals(3, info.getShape().length);
        assertEquals(1, info.getPosition());
    }

    @Test
    void testEmptyShape() {
        // Arrange
        int[][] emptyShape = new int[0][0];
        int position = 0;

        // Act
        NextShapeInfo info = new NextShapeInfo(emptyShape, position);

        // Assert
        assertEquals(0, info.getShape().length);
        assertEquals(0, info.getPosition());
    }
}

