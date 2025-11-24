package com.comp2042.game.bricks;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SBrickTest {

    @Test
    void getShapeMatrixReturnsListOfShapes() {
        SBrick brick = new SBrick();
        List<int[][]> shapes = brick.getShapeMatrix();
        
        assertNotNull(shapes);
        assertFalse(shapes.isEmpty());
    }

    @Test
    void getShapeMatrixReturnsTwoOrientations() {
        SBrick brick = new SBrick();
        List<int[][]> shapes = brick.getShapeMatrix();
        
        assertEquals(2, shapes.size(), "S-brick should have 2 orientations");
    }

    @Test
    void getShapeMatrixReturnsCorrectShapes() {
        SBrick brick = new SBrick();
        List<int[][]> shapes = brick.getShapeMatrix();
        
        for (int[][] shape : shapes) {
            assertEquals(4, shape.length);
            assertEquals(4, shape[0].length);
            // S-brick should have value 5
            boolean hasValue5 = false;
            for (int[] row : shape) {
                for (int cell : row) {
                    if (cell == 5) {
                        hasValue5 = true;
                    }
                }
            }
            assertTrue(hasValue5, "S-brick should contain value 5");
        }
    }

    @Test
    void getShapeMatrixReturnsDeepCopy() {
        SBrick brick = new SBrick();
        List<int[][]> shapes1 = brick.getShapeMatrix();
        List<int[][]> shapes2 = brick.getShapeMatrix();
        
        assertNotSame(shapes1, shapes2);
        assertNotSame(shapes1.get(0), shapes2.get(0));
    }
}

