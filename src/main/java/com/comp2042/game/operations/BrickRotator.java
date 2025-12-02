package com.comp2042.game.operations;

import com.comp2042.game.bricks.Brick;
import com.comp2042.game.data.NextShapeInfo;

/**
 * Manages brick rotation state and operations.
 * Tracks the current rotation position and provides methods to rotate bricks
 * through their various orientations.
 * 
 * <p>This class encapsulates rotation logic, making it easier to manage
 * brick transformations and maintain rotation state.
 */
public class BrickRotator {

    private Brick brick;
    private int currentShape = 0;

    /**
     * Calculates and returns information about the next rotation of the current brick.
     * Rotations cycle through all available orientations.
     * 
     * @return NextShapeInfo containing the shape matrix and position index
     *         of the next rotation
     */
    public NextShapeInfo getNextShape() {
        int nextShape = currentShape;
        nextShape = (++nextShape) % brick.getShapeMatrix().size();
        return new NextShapeInfo(brick.getShapeMatrix().get(nextShape), nextShape);
    }

    /**
     * Returns the shape matrix for the current rotation.
     * 
     * @return 2D array representing the brick's current orientation
     */
    public int[][] getCurrentShape() {
        return brick.getShapeMatrix().get(currentShape);
    }

    /**
     * Sets the current rotation position index.
     * 
     * @param currentShape the rotation index to set
     */
    public void setCurrentShape(int currentShape) {
        this.currentShape = currentShape;
    }

    /**
     * Sets a new brick and resets rotation to the initial position.
     * 
     * @param brick the brick to set for rotation management
     */
    public void setBrick(Brick brick) {
        this.brick = brick;
        currentShape = 0;
    }


}
