package com.comp2042.game.events;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MoveEventTest {

    @Test
    void constructorSetsEventTypeAndSource() {
        MoveEvent event = new MoveEvent(EventType.DOWN, EventSource.USER);
        
        assertEquals(EventType.DOWN, event.getEventType());
        assertEquals(EventSource.USER, event.getEventSource());
    }

    @Test
    void getEventTypeReturnsCorrectType() {
        MoveEvent event = new MoveEvent(EventType.LEFT, EventSource.USER);
        assertEquals(EventType.LEFT, event.getEventType());
    }

    @Test
    void getEventSourceReturnsCorrectSource() {
        MoveEvent event = new MoveEvent(EventType.RIGHT, EventSource.THREAD);
        assertEquals(EventSource.THREAD, event.getEventSource());
    }

    @Test
    void createEventWithAllEventTypes() {
        MoveEvent downEvent = new MoveEvent(EventType.DOWN, EventSource.USER);
        MoveEvent leftEvent = new MoveEvent(EventType.LEFT, EventSource.USER);
        MoveEvent rightEvent = new MoveEvent(EventType.RIGHT, EventSource.USER);
        MoveEvent rotateEvent = new MoveEvent(EventType.ROTATE, EventSource.USER);
        
        assertEquals(EventType.DOWN, downEvent.getEventType());
        assertEquals(EventType.LEFT, leftEvent.getEventType());
        assertEquals(EventType.RIGHT, rightEvent.getEventType());
        assertEquals(EventType.ROTATE, rotateEvent.getEventType());
    }

    @Test
    void createEventWithBothSources() {
        MoveEvent userEvent = new MoveEvent(EventType.DOWN, EventSource.USER);
        MoveEvent threadEvent = new MoveEvent(EventType.DOWN, EventSource.THREAD);
        
        assertEquals(EventSource.USER, userEvent.getEventSource());
        assertEquals(EventSource.THREAD, threadEvent.getEventSource());
    }
}

