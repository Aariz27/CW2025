package com.comp2042.game.data;

/**
 * Immutable data class representing the result of a downward movement.
 * Combines information about cleared rows with updated view data.
 * 
 * <p>This class follows the Data Transfer Object (DTO) pattern,
 * bundling related data that needs to be processed together after
 * a brick moves down or lands.
 */
public final class DownData {
    private final ClearRow clearRow;
    private final ViewData viewData;

    /**
     * Creates new down movement result data.
     * 
     * @param clearRow information about cleared rows and score bonus
     * @param viewData updated view rendering information
     */
    public DownData(ClearRow clearRow, ViewData viewData) {
        this.clearRow = clearRow;
        this.viewData = viewData;
    }

    /**
     * Returns information about cleared rows from this movement.
     * 
     * @return clear row data including line count and score bonus
     */
    public ClearRow getClearRow() {
        return clearRow;
    }

    /**
     * Returns the updated view data after the movement.
     * 
     * @return view data for rendering
     */
    public ViewData getViewData() {
        return viewData;
    }
}
