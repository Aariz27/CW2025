package com.comp2042.game.score;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Manages high score persistence and tracking.
 * Automatically saves and loads high score from disk, and tracks when
 * the current score beats the high score.
 * 
 * Uses Observer pattern via JavaFX IntegerProperty for UI binding.
 * Implements Singleton-like behavior through instance management.
 */
public final class HighScoreManager {
    
    private static final String HIGH_SCORE_FILE = "highscore.dat";
    private static final String APP_DIR = ".tetris";
    
    private final IntegerProperty highScore = new SimpleIntegerProperty(0);
    private boolean isNewHighScore = false;
    
    /**
     * Creates a new HighScoreManager.
     * Loads the saved high score from disk and observes the current score
     * to automatically detect when a new high score is achieved.
     * 
     * @param currentScore the current game score to monitor
     */
    public HighScoreManager(Score currentScore) {
        loadHighScore();
        
        // Listen for score changes to detect new high scores
        currentScore.scoreProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.intValue() > highScore.get()) {
                highScore.set(newVal.intValue());
                if (!isNewHighScore) {
                    isNewHighScore = true;
                }
            }
        });
    }
    
    /**
     * Returns the high score property for UI binding.
     * 
     * @return the IntegerProperty representing the high score
     */
    public IntegerProperty highScoreProperty() {
        return highScore;
    }
    
    /**
     * Gets the current high score value.
     * 
     * @return the current high score
     */
    public int getHighScore() {
        return highScore.get();
    }
    
    /**
     * Checks if the current game has achieved a new high score.
     * 
     * @return true if a new high score was achieved this game
     */
    public boolean isNewHighScore() {
        return isNewHighScore;
    }
    
    /**
     * Resets the new high score flag.
     * Called when starting a new game.
     */
    public void resetNewHighScoreFlag() {
        isNewHighScore = false;
    }
    
    /**
     * Saves the current high score to disk.
     * Creates the application directory if it doesn't exist.
     * High score is stored in user's home directory under .tetris/highscore.dat
     */
    public void saveHighScore() {
        try {
            // Create app directory if it doesn't exist
            Path appDir = Paths.get(System.getProperty("user.home"), APP_DIR);
            if (!Files.exists(appDir)) {
                Files.createDirectories(appDir);
            }
            
            // Write high score to file
            Path highScoreFile = appDir.resolve(HIGH_SCORE_FILE);
            try (DataOutputStream dos = new DataOutputStream(
                    new FileOutputStream(highScoreFile.toFile()))) {
                dos.writeInt(highScore.get());
            }
        } catch (IOException e) {
            System.err.println("Failed to save high score: " + e.getMessage());
        }
    }
    
    /**
     * Loads the high score from disk.
     * If the file doesn't exist or cannot be read, defaults to 0.
     */
    private void loadHighScore() {
        try {
            Path highScoreFile = Paths.get(System.getProperty("user.home"), APP_DIR, HIGH_SCORE_FILE);
            
            if (Files.exists(highScoreFile)) {
                try (DataInputStream dis = new DataInputStream(
                        new FileInputStream(highScoreFile.toFile()))) {
                    int savedHighScore = dis.readInt();
                    highScore.set(savedHighScore);
                }
            } else {
                // No saved high score, start at 0
                highScore.set(0);
            }
        } catch (IOException e) {
            System.err.println("Failed to load high score: " + e.getMessage());
            highScore.set(0);
        }
    }
    
    /**
     * Called when the game ends.
     * Saves the high score if it was beaten.
     */
    public void onGameEnd() {
        if (isNewHighScore) {
            saveHighScore();
        }
    }
}

