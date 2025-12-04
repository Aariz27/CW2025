package com.comp2042.game.level;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Manages level progression based on lines cleared.
 * Uses Strategy pattern to get level configurations and tracks current level
 * using JavaFX properties for reactive updates.
 * 
 * Design Patterns:
 * - Strategy Pattern: Uses LevelStrategy to get level configurations
 * - Observer Pattern: Uses IntegerProperty for level updates
 */
public final class LevelManager {
    
    private static final int LINES_PER_LEVEL = 5;
    
    private final LinesClearedTracker linesTracker;
    private final LevelStrategy levelStrategy;
    private final IntegerProperty currentLevel = new SimpleIntegerProperty(1);
    
    /**
     * Creates a new LevelManager.
     * 
     * @param linesTracker the tracker for total lines cleared
     * @param levelStrategy the strategy for getting level configurations
     */
    public LevelManager(LinesClearedTracker linesTracker, LevelStrategy levelStrategy) {
        this.linesTracker = linesTracker;
        this.levelStrategy = levelStrategy;
    }
    
    /**
     * Returns the level property for binding.
     * UI components can bind to this for automatic updates (Observer pattern).
     * 
     * @return the IntegerProperty containing current level
     */
    public IntegerProperty levelProperty() {
        return currentLevel;
    }
    
    /**
     * Gets the current level number.
     * 
     * @return the current level number
     */
    public int getCurrentLevel() {
        return currentLevel.getValue();
    }
    
    /**
     * Gets the current level configuration.
     * 
     * @return the Level configuration for the current level
     */
    public Level getCurrentLevelConfig() {
        return levelStrategy.getLevel(getCurrentLevel());
    }
    
    /**
     * Updates the level based on total lines cleared.
     * Level = (lines cleared / 5) + 1
     * Should be called whenever lines are cleared.
     * 
     * @return true if level increased, false otherwise
     */
    public boolean updateLevel() {
        int totalLines = linesTracker.getTotalLines();
        int newLevel = (totalLines / LINES_PER_LEVEL) + 1;
        int oldLevel = currentLevel.getValue();
        
        if (newLevel > oldLevel) {
            currentLevel.setValue(newLevel);
            return true; // Level increased
        }
        return false; // Level unchanged
    }

    /**
     * Sets the current level explicitly.
     *
     * @param level the level to set
     */
    public void setLevel(int level) {
        currentLevel.setValue(level);
    }
    
    /**
     * Resets the level manager to level 1.
     */
    public void reset() {
        currentLevel.setValue(1);
    }
}
