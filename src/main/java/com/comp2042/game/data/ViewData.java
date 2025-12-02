package com.comp2042.game.data;

import com.comp2042.game.operations.MatrixOperations;

/**
 * Immutable data transfer object containing all information needed to render the game view.
 * Includes the current brick position, shape, the next brick preview, and ghost brick position.
 * 
 * <p>This class follows the Data Transfer Object (DTO) pattern, encapsulating
 * related data and providing defensive copies to maintain immutability.
 */
public final class ViewData {

    private final int[][] brickData;
    private final int xPosition;
    private final int yPosition;
    private final int[][] nextBrickData;
    private final int ghostX;
    private final int ghostY;

    /**
     * Creates new view data with all rendering information.
     * 
     * @param brickData 2D array representing the current brick's shape
     * @param xPosition x-coordinate of the brick on the board
     * @param yPosition y-coordinate of the brick on the board
     * @param nextBrickData 2D array representing the next brick's shape
     * @param ghostX x-coordinate of the ghost brick (preview of landing position)
     * @param ghostY y-coordinate of the ghost brick (preview of landing position)
     */
    public ViewData(int[][] brickData, int xPosition, int yPosition, int[][] nextBrickData, int ghostX, int ghostY) {
        this.brickData = brickData;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.nextBrickData = nextBrickData;
        this.ghostX = ghostX;
        this.ghostY = ghostY;
    }

    /**
     * Returns a defensive copy of the current brick's shape matrix.
     * 
     * @return 2D array representing the brick shape
     */
    public int[][] getBrickData() {
        return MatrixOperations.copy(brickData);
    }

    /**
     * Returns the x-coordinate of the current brick.
     * 
     * @return x position on the board
     */
    public int getxPosition() {
        return xPosition;
    }

    /**
     * Returns the y-coordinate of the current brick.
     * 
     * @return y position on the board
     */
    public int getyPosition() {
        return yPosition;
    }

    /**
     * Returns a defensive copy of the next brick's shape matrix.
     * 
     * @return 2D array representing the next brick shape
     */
    public int[][] getNextBrickData() {
        return MatrixOperations.copy(nextBrickData);
    }

    /**
     * Returns the x-coordinate of the ghost brick.
     * 
     * @return x position of the ghost brick
     */
    public int getGhostX() {
        return ghostX;
    }

    /**
     * Returns the y-coordinate of the ghost brick.
     * 
     * @return y position of the ghost brick
     */
    public int getGhostY() {
        return ghostY;
    }
}
