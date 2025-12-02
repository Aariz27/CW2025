package com.comp2042.game.data;

import com.comp2042.game.operations.MatrixOperations;

/**
 * Immutable data class representing information about the next rotation of a brick.
 * Contains the shape matrix and the rotation position index.
 * 
 * <p>This class follows the Value Object pattern and is used by
 * {@link com.comp2042.game.operations.BrickRotator} to handle brick rotations.
 */
public final class NextShapeInfo {

    private final int[][] shape;
    private final int position;

    /**
     * Creates new shape information for rotation.
     * 
     * @param shape 2D array representing the brick shape after rotation
     * @param position index of this rotation in the brick's rotation list
     */
    public NextShapeInfo(final int[][] shape, final int position) {
        this.shape = shape;
        this.position = position;
    }

    /**
     * Returns a defensive copy of the shape matrix.
     * 
     * @return 2D array representing the rotated brick shape
     */
    public int[][] getShape() {
        return MatrixOperations.copy(shape);
    }

    /**
     * Returns the rotation position index.
     * 
     * @return rotation index in the brick's rotation list
     */
    public int getPosition() {
        return position;
    }
}
