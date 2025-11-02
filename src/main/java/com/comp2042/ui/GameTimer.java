package com.comp2042.ui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public final class GameTimer {
    private Timeline timeline;

    public void start(Runnable onTick, double intervalMs) {
        stop();
        timeline = new Timeline(new KeyFrame(Duration.millis(intervalMs), ae -> onTick.run()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public void stop() {
        if (timeline != null) {
            timeline.stop();
            timeline = null;
        }
    }
}