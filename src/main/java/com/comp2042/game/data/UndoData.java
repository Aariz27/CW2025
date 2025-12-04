package com.comp2042.game.data;

/**
 * Result wrapper for undo operations.
 */
public final class UndoData {
    private final boolean performed;
    private final ViewData viewData;
    private final int[][] boardMatrix;

    /**
     * Creates an undo result descriptor.
     *
     * @param performed whether the undo was applied
     * @param viewData the view data after undo (or current view if not performed)
     * @param boardMatrix the board state after undo attempt
     */
    public UndoData(boolean performed, ViewData viewData, int[][] boardMatrix) {
        this.performed = performed;
        this.viewData = viewData;
        this.boardMatrix = boardMatrix;
    }

    /**
     * Indicates whether the undo operation was performed.
     *
     * @return true if undo was applied; otherwise false
     */
    public boolean isPerformed() {
        return performed;
    }

    /**
     * Gets the view data after the undo attempt.
     *
     * @return view data to render
     */
    public ViewData getViewData() {
        return viewData;
    }

    /**
     * Gets the board matrix resulting from the undo attempt.
     *
     * @return board matrix
     */
    public int[][] getBoardMatrix() {
        return boardMatrix;
    }
}
