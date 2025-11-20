package com.comp2042.game.level;

import javafx.beans.property.IntegerProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LevelManagerTest {

    private LinesClearedTracker tracker;
    private LevelStrategy strategy;
    private LevelManager levelManager;

    @BeforeEach
    void setUp() {
        tracker = new LinesClearedTracker();
        strategy = new DefaultLevelStrategy();
        levelManager = new LevelManager(tracker, strategy);
    }

    @Test
    void constructorInitializesToLevel1() {
        assertEquals(1, levelManager.getCurrentLevel());
        assertEquals(1, levelManager.levelProperty().get());
    }

    @Test
    void getCurrentLevelReturnsCorrectLevel() {
        assertEquals(1, levelManager.getCurrentLevel());
    }

    @Test
    void getCurrentLevelConfigReturnsLevel1Config() {
        Level config = levelManager.getCurrentLevelConfig();
        
        assertEquals(1, config.getLevelNumber());
        assertEquals(400.0, config.getDropSpeedMs());
        assertEquals(1.0, config.getScoreMultiplier());
    }

    @Test
    void updateLevelStaysAtLevel1WithNoLines() {
        boolean levelUp = levelManager.updateLevel();
        
        assertFalse(levelUp);
        assertEquals(1, levelManager.getCurrentLevel());
    }

    @Test
    void updateLevelAdvancesToLevel2After5Lines() {
        tracker.addLines(5);
        boolean levelUp = levelManager.updateLevel();
        
        assertTrue(levelUp);
        assertEquals(2, levelManager.getCurrentLevel());
    }

    @Test
    void updateLevelAdvancesToLevel3After10Lines() {
        tracker.addLines(10);
        boolean levelUp = levelManager.updateLevel();
        
        assertTrue(levelUp);
        assertEquals(3, levelManager.getCurrentLevel());
    }

    @Test
    void updateLevelDoesNotAdvanceWith4Lines() {
        tracker.addLines(4);
        boolean levelUp = levelManager.updateLevel();
        
        assertFalse(levelUp);
        assertEquals(1, levelManager.getCurrentLevel());
    }

    @Test
    void updateLevelAdvancesMultipleLevels() {
        tracker.addLines(15);
        boolean levelUp = levelManager.updateLevel();
        
        assertTrue(levelUp);
        assertEquals(4, levelManager.getCurrentLevel());
    }

    @Test
    void updateLevelReturnsFalseWhenLevelUnchanged() {
        tracker.addLines(3);
        boolean levelUp = levelManager.updateLevel();
        
        assertFalse(levelUp);
        assertEquals(1, levelManager.getCurrentLevel());
    }

    @Test
    void updateLevelUpdatesProperty() {
        IntegerProperty property = levelManager.levelProperty();
        assertEquals(1, property.get());
        
        tracker.addLines(5);
        levelManager.updateLevel();
        
        assertEquals(2, property.get());
    }

    @Test
    void updateLevelOnlyAdvancesOncePerCall() {
        tracker.addLines(10);
        boolean firstLevelUp = levelManager.updateLevel();
        boolean secondLevelUp = levelManager.updateLevel();
        
        assertTrue(firstLevelUp);
        assertFalse(secondLevelUp, "Should not level up again in same call");
        assertEquals(3, levelManager.getCurrentLevel());
    }

    @Test
    void resetReturnsToLevel1() {
        tracker.addLines(10);
        levelManager.updateLevel();
        assertEquals(3, levelManager.getCurrentLevel());
        
        levelManager.reset();
        
        assertEquals(1, levelManager.getCurrentLevel());
        assertEquals(1, levelManager.levelProperty().get());
    }

    @Test
    void levelPropertyIsReactive() {
        IntegerProperty property = levelManager.levelProperty();
        
        tracker.addLines(5);
        levelManager.updateLevel();
        
        assertEquals(2, property.get());
    }

    @Test
    void getCurrentLevelConfigUpdatesWithLevel() {
        tracker.addLines(5);
        levelManager.updateLevel();
        
        Level config = levelManager.getCurrentLevelConfig();
        assertEquals(2, config.getLevelNumber());
    }
}

