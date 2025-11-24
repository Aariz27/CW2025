package com.comp2042.game.bricks;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ZBrickTest {

    @Test
    void getShapeMatrixReturnsListOfShapes() {
        ZBrick brick = new ZBrick();
        List<int[][]> shapes = brick.getShapeMatrix();
        
        assertNotNull(shapes);
        assertFalse(shapes.isEmpty());
    }

    @Test
    void getShapeMatrixReturnsTwoOrientations() {
        ZBrick brick = new ZBrick();
        List<int[][]> shapes = brick.getShapeMatrix();
        
        assertEquals(2, shapes.size(), "Z-brick should have 2 orientations");
    }

    @Test
    void getShapeMatrixReturnsCorrectShapes() {
        ZBrick brick = new ZBrick();
        List<int[][]> shapes = brick.getShapeMatrix();
        
        for (int[][] shape : shapes) {
            assertEquals(4, shape.length);
            assertEquals(4, shape[0].length);
            // Z-brick should have value 7
            boolean hasValue7 = false;
            for (int[] row : shape) {
                for (int cell : row) {
                    if (cell == 7) {
                        hasValue7 = true;
                    }
                }
            }
            assertTrue(hasValue7, "Z-brick should contain value 7");
        }
    }

    @Test
    void getShapeMatrixReturnsDeepCopy() {
        ZBrick brick = new ZBrick();
        List<int[][]> shapes1 = brick.getShapeMatrix();
        List<int[][]> shapes2 = brick.getShapeMatrix();
        
        assertNotSame(shapes1, shapes2);
        assertNotSame(shapes1.get(0), shapes2.get(0));
    }
}

