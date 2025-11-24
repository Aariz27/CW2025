package com.comp2042.ui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

/**
 * Manages the game timer for automatic piece dropping.
 * Extracted from GuiController following Single Responsibility Principle.
 */
public final class GameTimer {
    private Timeline timeline;

    /**
     * Starts the game timer with the specified interval.
     * 
     * @param onTick the callback to execute on each tick
     * @param intervalMs the interval in milliseconds between ticks
     */
    public void start(Runnable onTick, double intervalMs) {
        stop();
        timeline = new Timeline(new KeyFrame(Duration.millis(intervalMs), ae -> onTick.run()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    /**
     * Stops the game timer.
     */
    public void stop() {
        if (timeline != null) {
            timeline.stop();
            timeline = null;
        }
    }
}