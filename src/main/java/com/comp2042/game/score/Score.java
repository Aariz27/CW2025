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
     * Adds points to the score.
     * 
     * @param i the points to add
     */
    public void add(int i){
        score.setValue(score.getValue() + i);
    }

    /**
     * Resets the score to zero.
     */
    public void reset() {
        score.setValue(0);
    }
}
