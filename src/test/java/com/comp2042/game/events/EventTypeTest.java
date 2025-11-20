package com.comp2042.game.events;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EventTypeTest {

    @Test
    void eventTypeHasDownValue() {
        assertNotNull(EventType.DOWN);
        assertEquals("DOWN", EventType.DOWN.name());
    }

    @Test
    void eventTypeHasLeftValue() {
        assertNotNull(EventType.LEFT);
        assertEquals("LEFT", EventType.LEFT.name());
    }

    @Test
    void eventTypeHasRightValue() {
        assertNotNull(EventType.RIGHT);
        assertEquals("RIGHT", EventType.RIGHT.name());
    }

    @Test
    void eventTypeHasRotateValue() {
        assertNotNull(EventType.ROTATE);
        assertEquals("ROTATE", EventType.ROTATE.name());
    }

    @Test
    void eventTypeHasFourValues() {
        EventType[] values = EventType.values();
        assertEquals(4, values.length);
    }
}

