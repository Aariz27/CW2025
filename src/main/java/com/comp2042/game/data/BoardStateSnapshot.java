package com.comp2042.game.data;

import com.comp2042.game.bricks.Brick;
import com.comp2042.game.operations.MatrixOperations;

import java.awt.Point;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Immutable snapshot of board state used for undo functionality.
 */
public final class BoardStateSnapshot {
    private final int[][] boardMatrix;
    private final Point offset;
    private final Brick currentBrick;
    private final int rotationIndex;
    private final Brick heldBrick;
    private final Deque<Brick> queueSnapshot;
    private final int score;
    private final int totalLines;
    private final int level;

    /**
     * Creates an immutable snapshot of the board state for undo.
     *
     * @param boardMatrix the current board matrix
     * @param offset the active brick offset
     * @param currentBrick the active brick
     * @param rotationIndex the active brick rotation index
     * @param heldBrick the held brick (nullable)
     * @param queueSnapshot the upcoming brick queue snapshot (nullable)
     * @param score the current score
     * @param totalLines the total cleared lines
     * @param level the current level
     */
    public BoardStateSnapshot(int[][] boardMatrix,
                              Point offset,
                              Brick currentBrick,
                              int rotationIndex,
                              Brick heldBrick,
                              Deque<Brick> queueSnapshot,
                              int score,
                              int totalLines,
                              int level) {
        this.boardMatrix = MatrixOperations.copy(boardMatrix);
        this.offset = new Point(offset);
        this.currentBrick = currentBrick;
        this.rotationIndex = rotationIndex;
        this.heldBrick = heldBrick;
        this.queueSnapshot = queueSnapshot != null ? new ArrayDeque<>(queueSnapshot) : null;
        this.score = score;
        this.totalLines = totalLines;
        this.level = level;
    }

    public int[][] getBoardMatrix() {
        return MatrixOperations.copy(boardMatrix);
    }

    /**
     * Gets a copy of the board matrix at the time of the snapshot.
     *
     * @return copied board matrix
     */
    public Point getOffset() {
        return new Point(offset);
    }

    /**
     * Gets the active brick captured in the snapshot.
     *
     * @return the current brick
     */
    public Brick getCurrentBrick() {
        return currentBrick;
    }

    /**
     * Gets the rotation index of the active brick at snapshot time.
     *
     * @return rotation index
     */
    public int getRotationIndex() {
        return rotationIndex;
    }

    /**
     * Gets the held brick captured in the snapshot.
     *
     * @return held brick or null if none
     */
    public Brick getHeldBrick() {
        return heldBrick;
    }

    /**
     * Gets a copy of the upcoming brick queue from the snapshot.
     *
     * @return queue snapshot or null when unavailable
     */
    public Deque<Brick> getQueueSnapshot() {
        return queueSnapshot != null ? new ArrayDeque<>(queueSnapshot) : null;
    }

    /**
     * Gets the score value at the time of the snapshot.
     *
     * @return score value
     */
    public int getScore() {
        return score;
    }

    /**
     * Gets the total cleared lines at the time of the snapshot.
     *
     * @return total cleared lines
     */
    public int getTotalLines() {
        return totalLines;
    }

    /**
     * Gets the level number at the time of the snapshot.
     *
     * @return level value
     */
    public int getLevel() {
        return level;
    }
}
