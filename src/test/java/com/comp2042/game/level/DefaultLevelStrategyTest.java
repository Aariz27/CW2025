package com.comp2042.game.level;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DefaultLevelStrategyTest {

    private DefaultLevelStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new DefaultLevelStrategy();
    }

    @Test
    void getLevelReturnsCorrectLevelForLevel1() {
        Level level = strategy.getLevel(1);
        
        assertEquals(1, level.getLevelNumber());
        assertEquals(400.0, level.getDropSpeedMs());
        assertEquals(1.0, level.getScoreMultiplier());
    }

    @Test
    void getLevelReturnsCorrectLevelForLevel5() {
        Level level = strategy.getLevel(5);
        
        assertEquals(5, level.getLevelNumber());
        assertEquals(200.0, level.getDropSpeedMs());
        assertEquals(2.5, level.getScoreMultiplier());
    }

    @Test
    void getLevelReturnsCorrectLevelForLevel10() {
        Level level = strategy.getLevel(10);
        
        assertEquals(10, level.getLevelNumber());
        assertEquals(60.0, level.getDropSpeedMs());
        assertEquals(2.5, level.getScoreMultiplier());
    }

    @Test
    void getLevelReturnsCalculatedLevelForLevel11() {
        Level level = strategy.getLevel(11);
        
        assertEquals(11, level.getLevelNumber());
        assertEquals(60.0, level.getDropSpeedMs(), "Speed should cap at minimum");
        assertEquals(2.5, level.getScoreMultiplier());
    }

    @Test
    void getLevelReturnsCalculatedLevelForHighLevel() {
        Level level = strategy.getLevel(50);
        
        assertEquals(50, level.getLevelNumber());
        assertEquals(60.0, level.getDropSpeedMs());
        assertEquals(2.5, level.getScoreMultiplier());
    }

    @Test
    void getLevelThrowsExceptionForInvalidLevel() {
        assertThrows(IllegalArgumentException.class, () -> {
            strategy.getLevel(0);
        });
    }

    @Test
    void getLevelShowsProgressionFromLevel1To10() {
        // Test progression: speed decreases, multiplier increases
        Level level1 = strategy.getLevel(1);
        Level level2 = strategy.getLevel(2);
        Level level3 = strategy.getLevel(3);
        
        assertTrue(level1.getDropSpeedMs() > level2.getDropSpeedMs());
        assertTrue(level2.getDropSpeedMs() > level3.getDropSpeedMs());
        assertTrue(level1.getScoreMultiplier() < level2.getScoreMultiplier());
        assertTrue(level2.getScoreMultiplier() < level3.getScoreMultiplier());
    }

    @Test
    void getLevelMultiplierCapsAtLevel5() {
        Level level5 = strategy.getLevel(5);
        Level level6 = strategy.getLevel(6);
        Level level7 = strategy.getLevel(7);
        
        assertEquals(2.5, level5.getScoreMultiplier());
        assertEquals(2.5, level6.getScoreMultiplier());
        assertEquals(2.5, level7.getScoreMultiplier());
    }
}

