package com.comp2042.game.level;

/**
 * Represents a game level with its configuration.
 * Immutable class that holds level-specific settings like drop speed and score multiplier.
 */
public final class Level {
    
    private final int levelNumber;
    private final double dropSpeedMs;
    private final double scoreMultiplier;
    
    /**
     * Creates a new level configuration.
     * 
     * @param levelNumber the level number (1, 2, 3, etc.)
     * @param dropSpeedMs the drop speed in milliseconds (lower = faster)
     * @param scoreMultiplier the score multiplier for this level
     */
    public Level(int levelNumber, double dropSpeedMs, double scoreMultiplier) {
        this.levelNumber = levelNumber;
        this.dropSpeedMs = dropSpeedMs;
        this.scoreMultiplier = scoreMultiplier;
    }
    
    /**
     * Gets the level number.
     * 
     * @return the level number
     */
    public int getLevelNumber() {
        return levelNumber;
    }
    
    /**
     * Gets the drop speed in milliseconds.
     * Lower values mean faster dropping.
     * 
     * @return the drop speed in milliseconds
     */
    public double getDropSpeedMs() {
        return dropSpeedMs;
    }
    
    /**
     * Gets the score multiplier for this level.
     * 
     * @return the score multiplier
     */
    public double getScoreMultiplier() {
        return scoreMultiplier;
    }
}

