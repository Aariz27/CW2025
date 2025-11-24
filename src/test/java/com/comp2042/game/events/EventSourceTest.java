package com.comp2042.game.events;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EventSourceTest {

    @Test
    void eventSourceHasUserValue() {
        assertNotNull(EventSource.USER);
        assertEquals("USER", EventSource.USER.name());
    }

    @Test
    void eventSourceHasThreadValue() {
        assertNotNull(EventSource.THREAD);
        assertEquals("THREAD", EventSource.THREAD.name());
    }

    @Test
    void eventSourceHasTwoValues() {
        EventSource[] values = EventSource.values();
        assertEquals(2, values.length);
    }
}

