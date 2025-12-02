package com.comp2042.game.bricks;

import java.util.List;

/**
 * Interface representing a Tetris brick (tetromino).
 * Each brick type has one or more rotation states represented as 2D matrices.
 * 
 * <p>Implementations include the seven standard Tetris pieces:
 * I, J, L, O, S, T, and Z bricks. Each provides its rotation matrices
 * where non-zero values represent filled cells.
 */
public interface Brick {

    /**
     * Returns the list of all rotation states for this brick.
     * Each rotation is represented as a 2D integer array where:
     * <ul>
     *   <li>0 represents an empty cell</li>
     *   <li>Non-zero values represent filled cells (color-coded)</li>
     * </ul>
     * 
     * @return list of 2D arrays, each representing one rotation state
     */
    List<int[][]> getShapeMatrix();
}
