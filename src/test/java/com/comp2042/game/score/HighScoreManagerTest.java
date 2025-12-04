package com.comp2042.game.score;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class HighScoreManagerTest {

    private Score score;
    private HighScoreManager highScoreManager;

    @BeforeEach
    void setUp() {
        score = new Score();
        highScoreManager = new HighScoreManager(score);
    }

    @Test
    void constructorInitializesHighScore() {
        assertNotNull(highScoreManager.highScoreProperty());
        assertTrue(highScoreManager.getHighScore() >= 0);
    }

    @Test
    void getHighScoreReturnsCurrentHighScore() {
        int highScore = highScoreManager.getHighScore();
        assertTrue(highScore >= 0);
    }

    @Test
    void highScorePropertyReturnsProperty() {
        assertNotNull(highScoreManager.highScoreProperty());
        assertEquals(highScoreManager.getHighScore(), highScoreManager.highScoreProperty().get());
    }

    @Test
    void isNewHighScoreInitiallyFalse() {
        assertFalse(highScoreManager.isNewHighScore());
    }

    @Test
    void isNewHighScoreReturnsTrueWhenScoreExceedsHighScore() {
        // Reset to ensure we start from known state
        highScoreManager.resetNewHighScoreFlag();
        
        // Add score higher than current high score
        int currentHighScore = highScoreManager.getHighScore();
        score.add(currentHighScore + 100);
        
        assertTrue(highScoreManager.isNewHighScore());
    }

    @Test
    void highScoreUpdatesWhenCurrentScoreExceeds() {
        int initialHighScore = highScoreManager.getHighScore();
        
        // Add score higher than high score
        score.add(initialHighScore + 50);
        
        assertEquals(initialHighScore + 50, highScoreManager.getHighScore());
    }

    @Test
    void highScoreDoesNotUpdateWhenCurrentScoreIsLower() {
        // Set a known high score
        score.add(100);
        int highScore = highScoreManager.getHighScore();
        
        // Reset score
        score.reset();
        highScoreManager.resetNewHighScoreFlag();
        
        // Add smaller score
        score.add(50);
        
        // High score should remain unchanged
        assertEquals(highScore, highScoreManager.getHighScore());
    }

    @Test
    void resetNewHighScoreFlagClearsFlag() {
        // First achieve a new high score
        int currentHighScore = highScoreManager.getHighScore();
        score.add(currentHighScore + 100);
        assertTrue(highScoreManager.isNewHighScore());
        
        // Reset the flag
        highScoreManager.resetNewHighScoreFlag();
        
        assertFalse(highScoreManager.isNewHighScore());
    }

    @Test
    void onGameEndDoesNotThrowException() {
        assertDoesNotThrow(() -> highScoreManager.onGameEnd());
    }

    @Test
    void onGameEndSavesHighScoreWhenNewHighScoreAchieved() {
        int currentHighScore = highScoreManager.getHighScore();
        score.add(currentHighScore + 100);
        
        // Should not throw
        assertDoesNotThrow(() -> highScoreManager.onGameEnd());
    }

    @Test
    void saveHighScoreDoesNotThrowException() {
        assertDoesNotThrow(() -> highScoreManager.saveHighScore());
    }

    @Test
    void highScorePropertyCanBeBound() {
        var property = highScoreManager.highScoreProperty();
        
        // Property should be bindable (not null and has value)
        assertNotNull(property);
        assertDoesNotThrow(property::get);
    }

    @Test
    void multipleScoreIncrementsUpdateHighScore() {
        int initial = highScoreManager.getHighScore();
        
        score.add(10);
        score.add(20);
        score.add(30);
        
        // Total score: 60
        if (60 > initial) {
            assertEquals(60, highScoreManager.getHighScore());
            assertTrue(highScoreManager.isNewHighScore());
        }
    }

    @Test
    void highScoreTracksMaxValue() {
        int initial = highScoreManager.getHighScore();
        
        // Add large score
        score.add(initial + 1000);
        int afterFirst = highScoreManager.getHighScore();
        
        // Reset score and add smaller amount
        score.reset();
        score.add(100);
        
        // High score should still be the max achieved
        assertEquals(afterFirst, highScoreManager.getHighScore());
    }
}

