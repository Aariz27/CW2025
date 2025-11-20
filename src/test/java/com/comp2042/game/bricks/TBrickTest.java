package com.comp2042.game.bricks;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TBrickTest {

    @Test
    void getShapeMatrixReturnsListOfShapes() {
        TBrick brick = new TBrick();
        List<int[][]> shapes = brick.getShapeMatrix();
        
        assertNotNull(shapes);
        assertFalse(shapes.isEmpty());
    }

    @Test
    void getShapeMatrixReturnsFourOrientations() {
        TBrick brick = new TBrick();
        List<int[][]> shapes = brick.getShapeMatrix();
        
        assertEquals(4, shapes.size(), "T-brick should have 4 orientations");
    }

    @Test
    void getShapeMatrixReturnsCorrectShapes() {
        TBrick brick = new TBrick();
        List<int[][]> shapes = brick.getShapeMatrix();
        
        for (int[][] shape : shapes) {
            assertEquals(4, shape.length);
            assertEquals(4, shape[0].length);
            // T-brick should have value 6
            boolean hasValue6 = false;
            for (int[] row : shape) {
                for (int cell : row) {
                    if (cell == 6) {
                        hasValue6 = true;
                    }
                }
            }
            assertTrue(hasValue6, "T-brick should contain value 6");
        }
    }

    @Test
    void getShapeMatrixReturnsDeepCopy() {
        TBrick brick = new TBrick();
        List<int[][]> shapes1 = brick.getShapeMatrix();
        List<int[][]> shapes2 = brick.getShapeMatrix();
        
        assertNotSame(shapes1, shapes2);
        assertNotSame(shapes1.get(0), shapes2.get(0));
    }
}

