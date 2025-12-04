package com.comp2042.game.data;

/**
 * Result wrapper for undo operations.
 */
public final class UndoData {
    private final boolean performed;
    private final ViewData viewData;
    private final int[][] boardMatrix;

    public UndoData(boolean performed, ViewData viewData, int[][] boardMatrix) {
        this.performed = performed;
        this.viewData = viewData;
        this.boardMatrix = boardMatrix;
    }

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
