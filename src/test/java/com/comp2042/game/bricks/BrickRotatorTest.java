package com.comp2042.game.bricks;

import com.comp2042.game.operations.BrickRotator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BrickRotatorTest {

    private BrickRotator rotator;

    @BeforeEach
    void setUp() {
        rotator = new BrickRotator();
        rotator.setBrick(new IBrick());
    }

    @Test
    void getCurrentShapeReturnsFirstOrientation() {
        int[][] currentShape = rotator.getCurrentShape();

        assertNotNull(currentShape);
        assertEquals(4, currentShape.length);
        assertEquals(4, currentShape[0].length);
        assertEquals(1, currentShape[1][0]);
        assertEquals(1, currentShape[1][3]);
    }

    @Test
    void setCurrentShapeSwitchesOrientation() {
        int[][] initialShape = rotator.getCurrentShape();
        var nextInfo = rotator.getNextShape();

        rotator.setCurrentShape(nextInfo.getPosition());
        int[][] rotated = rotator.getCurrentShape();

        assertNotNull(rotated);
        assertNotSame(initialShape, rotated);
        assertEquals(nextInfo.getShape()[0][1], rotated[0][1]);
    }
}

