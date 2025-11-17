package com.comp2042.game.board;

/**
 * Main board interface composed of segregated interfaces.
 * Follows Interface Segregation Principle - clients can depend on specific
 * interfaces (e.g., MovableBoard) instead of the full Board interface.
 */
public interface Board extends MovableBoard, ClearableBoard, SpawnableBoard, ScorableBoard {
    // All methods inherited from segregated interfaces
}
