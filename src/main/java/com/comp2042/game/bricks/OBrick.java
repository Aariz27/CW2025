package com.comp2042.game.bricks;

import com.comp2042.game.operations.MatrixOperations;

import java.util.ArrayList;
import java.util.List;

/**
 * The O-shaped tetromino (square block).
 * This brick has only one rotation state as it's symmetrical.
 * Represented by the value 4 in the matrix.
 */
final class OBrick implements Brick {

    private final List<int[][]> brickMatrix = new ArrayList<>();

    /**
     * Constructs an O-brick with its single rotation state.
     */
    public OBrick() {
        brickMatrix.add(new int[][]{
                {0, 0, 0, 0},
                {0, 4, 4, 0},
                {0, 4, 4, 0},
                {0, 0, 0, 0}
        });
    }

    @Override
    public List<int[][]> getShapeMatrix() {
        return MatrixOperations.deepCopyList(brickMatrix);
    }

}
