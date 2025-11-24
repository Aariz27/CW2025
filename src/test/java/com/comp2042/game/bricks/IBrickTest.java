package com.comp2042.game.bricks;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IBrickTest {

    @Test
    void getShapeMatrixReturnsListOfShapes() {
        IBrick brick = new IBrick();
        List<int[][]> shapes = brick.getShapeMatrix();
        
        assertNotNull(shapes);
        assertFalse(shapes.isEmpty());
    }

    @Test
    void getShapeMatrixReturnsTwoOrientations() {
        IBrick brick = new IBrick();
        List<int[][]> shapes = brick.getShapeMatrix();
        
        assertEquals(2, shapes.size(), "I-brick should have 2 orientations");
    }

    @Test
    void getShapeMatrixReturnsCorrectHorizontalShape() {
        IBrick brick = new IBrick();
        List<int[][]> shapes = brick.getShapeMatrix();
        int[][] horizontal = shapes.get(0);
        
        assertEquals(4, horizontal.length);
        assertEquals(4, horizontal[0].length);
        // Check horizontal line pattern
        assertEquals(1, horizontal[1][0]);
        assertEquals(1, horizontal[1][1]);
        assertEquals(1, horizontal[1][2]);
        assertEquals(1, horizontal[1][3]);
    }

    @Test
    void getShapeMatrixReturnsCorrectVerticalShape() {
        IBrick brick = new IBrick();
        List<int[][]> shapes = brick.getShapeMatrix();
        int[][] vertical = shapes.get(1);
        
        assertEquals(4, vertical.length);
        assertEquals(4, vertical[0].length);
        // Check vertical line pattern
        assertEquals(1, vertical[0][1]);
        assertEquals(1, vertical[1][1]);
        assertEquals(1, vertical[2][1]);
        assertEquals(1, vertical[3][1]);
    }

    @Test
    void getShapeMatrixReturnsDeepCopy() {
        IBrick brick = new IBrick();
        List<int[][]> shapes1 = brick.getShapeMatrix();
        List<int[][]> shapes2 = brick.getShapeMatrix();
        
        assertNotSame(shapes1, shapes2, "Should return new list instance");
        assertNotSame(shapes1.get(0), shapes2.get(0), "Should return new array instance");
    }

    @Test
    void getShapeMatrixModificationDoesNotAffectOriginal() {
        IBrick brick = new IBrick();
        List<int[][]> shapes = brick.getShapeMatrix();
        int[][] shape = shapes.get(0);
        int originalValue = shape[1][0];
        
        shape[1][0] = 99;
        
        List<int[][]> newShapes = brick.getShapeMatrix();
        assertEquals(originalValue, newShapes.get(0)[1][0], "Modification should not affect original");
    }
}

