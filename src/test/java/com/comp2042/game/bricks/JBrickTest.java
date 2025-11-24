package com.comp2042.game.bricks;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JBrickTest {

    @Test
    void getShapeMatrixReturnsListOfShapes() {
        JBrick brick = new JBrick();
        List<int[][]> shapes = brick.getShapeMatrix();
        
        assertNotNull(shapes);
        assertFalse(shapes.isEmpty());
    }

    @Test
    void getShapeMatrixReturnsFourOrientations() {
        JBrick brick = new JBrick();
        List<int[][]> shapes = brick.getShapeMatrix();
        
        assertEquals(4, shapes.size(), "J-brick should have 4 orientations");
    }

    @Test
    void getShapeMatrixReturnsCorrectShapes() {
        JBrick brick = new JBrick();
        List<int[][]> shapes = brick.getShapeMatrix();
        
        for (int[][] shape : shapes) {
            assertEquals(4, shape.length);
            assertEquals(4, shape[0].length);
            // J-brick should have value 2
            boolean hasValue2 = false;
            for (int[] row : shape) {
                for (int cell : row) {
                    if (cell == 2) {
                        hasValue2 = true;
                    }
                }
            }
            assertTrue(hasValue2, "J-brick should contain value 2");
        }
    }

    @Test
    void getShapeMatrixReturnsDeepCopy() {
        JBrick brick = new JBrick();
        List<int[][]> shapes1 = brick.getShapeMatrix();
        List<int[][]> shapes2 = brick.getShapeMatrix();
        
        assertNotSame(shapes1, shapes2);
        assertNotSame(shapes1.get(0), shapes2.get(0));
    }
}

