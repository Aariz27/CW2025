package com.comp2042.game.score;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Manages the game score using JavaFX properties.
 * Implements Observer pattern - UI components bind to scoreProperty() to
 * automatically receive updates when score changes.
 */
public final class Score {

    private final IntegerProperty score = new SimpleIntegerProperty(0);

    /**
     * Returns the score property for binding.
     * UI components can bind to this for automatic updates (Observer pattern).
     */
    public IntegerProperty scoreProperty() {
        return score;
    }

    /**
     * Gets the current score value.
     *
     * @return the score
     */
    public int get() {
        return score.getValue();
    }

    /**
     * Adds points to the score.
     * 
     * @param i the points to add
     */
    public void add(int i){
        score.setValue(score.getValue() + i);
    }

    /**
     * Sets the score to the provided value.
     *
     * @param value the new score value
     */
    public void set(int value) {
        score.setValue(value);
    }

    /**
     * Resets the score to zero.
     */
    public void reset() {
        score.setValue(0);
    }
}
