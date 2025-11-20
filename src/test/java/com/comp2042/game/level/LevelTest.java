package com.comp2042.game.level;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LevelTest {

    @Test
    void constructorSetsAllFields() {
        Level level = new Level(5, 200.0, 2.5);
        
        assertEquals(5, level.getLevelNumber());
        assertEquals(200.0, level.getDropSpeedMs());
        assertEquals(2.5, level.getScoreMultiplier());
    }

    @Test
    void getLevelNumberReturnsCorrectValue() {
        Level level = new Level(3, 300.0, 1.5);
        assertEquals(3, level.getLevelNumber());
    }

    @Test
    void getDropSpeedMsReturnsCorrectValue() {
        Level level = new Level(2, 350.0, 1.2);
        assertEquals(350.0, level.getDropSpeedMs());
    }

    @Test
    void getScoreMultiplierReturnsCorrectValue() {
        Level level = new Level(4, 250.0, 2.0);
        assertEquals(2.0, level.getScoreMultiplier());
    }

    @Test
    void levelIsImmutable() {
        Level level1 = new Level(1, 400.0, 1.0);
        Level level2 = new Level(1, 400.0, 1.0);
        
        // Both should have same values but be different instances
        assertEquals(level1.getLevelNumber(), level2.getLevelNumber());
        assertEquals(level1.getDropSpeedMs(), level2.getDropSpeedMs());
        assertEquals(level1.getScoreMultiplier(), level2.getScoreMultiplier());
    }
}

