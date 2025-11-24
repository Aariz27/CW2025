package com.comp2042.game.bricks;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OBrickTest {

    @Test
    void getShapeMatrixReturnsListOfShapes() {
        OBrick brick = new OBrick();
        List<int[][]> shapes = brick.getShapeMatrix();
        
        assertNotNull(shapes);
        assertFalse(shapes.isEmpty());
    }

    @Test
    void getShapeMatrixReturnsOneOrientation() {
        OBrick brick = new OBrick();
        List<int[][]> shapes = brick.getShapeMatrix();
        
        assertEquals(1, shapes.size(), "O-brick should have 1 orientation (square)");
    }

    @Test
    void getShapeMatrixReturnsCorrectSquareShape() {
        OBrick brick = new OBrick();
        List<int[][]> shapes = brick.getShapeMatrix();
        int[][] shape = shapes.get(0);
        
        assertEquals(4, shape.length);
        assertEquals(4, shape[0].length);
        // Check 2x2 square pattern
        assertEquals(4, shape[1][1]);
        assertEquals(4, shape[1][2]);
        assertEquals(4, shape[2][1]);
        assertEquals(4, shape[2][2]);
    }

    @Test
    void getShapeMatrixReturnsDeepCopy() {
        OBrick brick = new OBrick();
        List<int[][]> shapes1 = brick.getShapeMatrix();
        List<int[][]> shapes2 = brick.getShapeMatrix();
        
        assertNotSame(shapes1, shapes2);
        assertNotSame(shapes1.get(0), shapes2.get(0));
    }
}

