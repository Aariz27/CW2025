package com.comp2042.game.level;

/**
 * Default implementation of LevelStrategy.
 * Provides standard level progression with increasing difficulty:
 * - Drop speed decreases (faster) as level increases
 * - Score multiplier increases as level increases
 * 
 * Design Patterns:
 * - Strategy Pattern: Concrete implementation of LevelStrategy interface
 */
public class DefaultLevelStrategy implements LevelStrategy {
    
    // Minimum drop speed (maximum difficulty)
    private static final double MIN_DROP_SPEED_MS = 60.0;
    
    // Maximum level with predefined configuration
    private static final int MAX_PREDEFINED_LEVEL = 10;
    
    /**
     * Gets the level configuration for the specified level number.
     * Uses predefined configurations for levels 1-10, then calculates
     * for higher levels.
     * 
     * @param levelNumber the level number (1, 2, 3, etc.)
     * @return the Level configuration for that level
     */
    @Override
    public Level getLevel(int levelNumber) {
        if (levelNumber <= MAX_PREDEFINED_LEVEL) {
            return getPredefinedLevel(levelNumber);
        } else {
            return getCalculatedLevel(levelNumber);
        }
    }
    
    /**
     * Gets predefined level configuration for levels 1-10.
     * 
     * @param levelNumber the level number (1-10)
     * @return the Level configuration
     */
    private Level getPredefinedLevel(int levelNumber) {
        return switch (levelNumber) {
            case 1 -> new Level(1, 400.0, 1.0);
            case 2 -> new Level(2, 350.0, 1.2);
            case 3 -> new Level(3, 300.0, 1.5);
            case 4 -> new Level(4, 250.0, 2.0);
            case 5 -> new Level(5, 200.0, 2.5);
            case 6 -> new Level(6, 150.0, 2.5);
            case 7 -> new Level(7, 120.0, 2.5);
            case 8 -> new Level(8, 100.0, 2.5);
            case 9 -> new Level(9, 80.0, 2.5);
            case 10 -> new Level(10, 60.0, 2.5);
            default -> throw new IllegalArgumentException("Invalid level number: " + levelNumber);
        };
    }
    
    /**
     * Calculates level configuration for levels beyond 10.
     * Speed caps at minimum, multiplier stays at 2.5x.
     * 
     * @param levelNumber the level number (> 10)
     * @return the Level configuration
     */
    private Level getCalculatedLevel(int levelNumber) {
        // Speed is capped at minimum for very high levels
        double speed = MIN_DROP_SPEED_MS;
        // Multiplier stays at 2.5x for levels 5+
        double multiplier = 2.5;
        return new Level(levelNumber, speed, multiplier);
    }
}

