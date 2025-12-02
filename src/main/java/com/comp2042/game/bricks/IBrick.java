package com.comp2042.game.bricks;

import com.comp2042.game.operations.MatrixOperations;

import java.util.ArrayList;
import java.util.List;

/**
 * The I-shaped tetromino (straight line piece).
 * This brick has two rotation states: horizontal and vertical.
 * Represented by the value 1 in the matrix.
 */
final class IBrick implements Brick {

    private final List<int[][]> brickMatrix = new ArrayList<>();

    /**
     * Constructs an I-brick with its two rotation states.
     */
    public IBrick() {
        brickMatrix.add(new int[][]{
                {0, 0, 0, 0},
                {1, 1, 1, 1},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        });
        brickMatrix.add(new int[][]{
                {0, 1, 0, 0},
                {0, 1, 0, 0},
                {0, 1, 0, 0},
                {0, 1, 0, 0}
        });
    }

    @Override
    public List<int[][]> getShapeMatrix() {
        return MatrixOperations.deepCopyList(brickMatrix);
    }

}
