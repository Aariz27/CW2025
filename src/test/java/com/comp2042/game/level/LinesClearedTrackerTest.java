package com.comp2042.game.level;

import javafx.beans.property.IntegerProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LinesClearedTrackerTest {

    private LinesClearedTracker tracker;

    @BeforeEach
    void setUp() {
        tracker = new LinesClearedTracker();
    }

    @Test
    void constructorInitializesToZero() {
        assertEquals(0, tracker.getTotalLines());
        assertEquals(0, tracker.linesProperty().get());
    }

    @Test
    void addLinesIncreasesTotal() {
        tracker.addLines(3);
        
        assertEquals(3, tracker.getTotalLines());
        assertEquals(3, tracker.linesProperty().get());
    }

    @Test
    void addLinesAccumulatesMultipleCalls() {
        tracker.addLines(2);
        tracker.addLines(3);
        tracker.addLines(1);
        
        assertEquals(6, tracker.getTotalLines());
    }

    @Test
    void addLinesUpdatesProperty() {
        IntegerProperty property = tracker.linesProperty();
        assertEquals(0, property.get());
        
        tracker.addLines(5);
        
        assertEquals(5, property.get());
    }

    @Test
    void resetClearsTotal() {
        tracker.addLines(10);
        assertEquals(10, tracker.getTotalLines());
        
        tracker.reset();
        
        assertEquals(0, tracker.getTotalLines());
        assertEquals(0, tracker.linesProperty().get());
    }

    @Test
    void resetAfterMultipleAdds() {
        tracker.addLines(2);
        tracker.addLines(3);
        tracker.addLines(5);
        
        tracker.reset();
        
        assertEquals(0, tracker.getTotalLines());
    }

    @Test
    void linesPropertyIsReactive() {
        IntegerProperty property = tracker.linesProperty();
        
        tracker.addLines(7);
        
        assertEquals(7, property.get());
    }
}

