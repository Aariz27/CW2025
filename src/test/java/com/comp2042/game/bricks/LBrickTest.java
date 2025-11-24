package com.comp2042.game.bricks;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LBrickTest {

    @Test
    void getShapeMatrixReturnsListOfShapes() {
        LBrick brick = new LBrick();
        List<int[][]> shapes = brick.getShapeMatrix();
        
        assertNotNull(shapes);
        assertFalse(shapes.isEmpty());
    }

    @Test
    void getShapeMatrixReturnsFourOrientations() {
        LBrick brick = new LBrick();
        List<int[][]> shapes = brick.getShapeMatrix();
        
        assertEquals(4, shapes.size(), "L-brick should have 4 orientations");
    }

    @Test
    void getShapeMatrixReturnsCorrectShapes() {
        LBrick brick = new LBrick();
        List<int[][]> shapes = brick.getShapeMatrix();
        
        for (int[][] shape : shapes) {
            assertEquals(4, shape.length);
            assertEquals(4, shape[0].length);
            // L-brick should have value 3
            boolean hasValue3 = false;
            for (int[] row : shape) {
                for (int cell : row) {
                    if (cell == 3) {
                        hasValue3 = true;
                    }
                }
            }
            assertTrue(hasValue3, "L-brick should contain value 3");
        }
    }

    @Test
    void getShapeMatrixReturnsDeepCopy() {
        LBrick brick = new LBrick();
        List<int[][]> shapes1 = brick.getShapeMatrix();
        List<int[][]> shapes2 = brick.getShapeMatrix();
        
        assertNotSame(shapes1, shapes2);
        assertNotSame(shapes1.get(0), shapes2.get(0));
    }
}

