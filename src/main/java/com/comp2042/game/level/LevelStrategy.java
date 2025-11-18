package com.comp2042.game.level;

/**
 * Strategy interface for level configurations.
 * Allows different level progression schemes to be implemented
 * without modifying existing code (Open/Closed Principle).
 * 
 * Design Patterns:
 * - Strategy Pattern: Different level progression algorithms can be
 *   implemented (linear, exponential, custom) and swapped at runtime.
 */
public interface LevelStrategy {
    
    /**
     * Gets the level configuration for the specified level number.
     * 
     * @param levelNumber the level number (1, 2, 3, etc.)
     * @return the Level configuration for that level
     */
    Level getLevel(int levelNumber);
}

