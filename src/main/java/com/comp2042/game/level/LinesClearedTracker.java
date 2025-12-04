package com.comp2042.game.level;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Tracks the total number of lines cleared across the game.
 * Uses JavaFX properties for reactive updates, following Observer pattern.
 */
public final class LinesClearedTracker {
    
    private final IntegerProperty totalLines = new SimpleIntegerProperty(0);

    /**
     * Returns the lines property for binding.
     * UI components can bind to this for automatic updates (Observer pattern).
     * 
     * @return the IntegerProperty containing total lines cleared
     */
    public IntegerProperty linesProperty() {
        return totalLines;
    }

    /**
     * Adds lines to the total count.
     * 
     * @param lines the number of lines to add
     */
    public void addLines(int lines) {
        totalLines.setValue(totalLines.getValue() + lines);
    }

    /**
     * Gets the current total number of lines cleared.
     * 
     * @return the total lines cleared
     */
    public int getTotalLines() {
        return totalLines.getValue();
    }

    /**
     * Sets the total lines cleared to the provided value.
     *
     * @param total the total lines to set
     */
    public void setTotalLines(int total) {
        totalLines.setValue(total);
    }

    /**
     * Resets the lines counter to zero.
     */
    public void reset() {
        totalLines.setValue(0);
    }
}
