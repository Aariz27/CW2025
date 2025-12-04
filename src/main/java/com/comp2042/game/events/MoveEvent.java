package com.comp2042.game.events;

/**
 * Immutable data class representing a movement event in the game.
 * Contains both the type of movement and the source that triggered it.
 * 
 * <p>This class follows the Value Object pattern - it's immutable and
 * serves purely to transport data between components.
 */
public final class MoveEvent {
    private final EventType eventType;
    private final EventSource eventSource;

    /**
     * Creates a new movement event.
     * 
     * @param eventType the type of movement (DOWN, LEFT, RIGHT, ROTATE, HARD_DROP)
     * @param eventSource the source of the event (USER or THREAD)
     */
    public MoveEvent(EventType eventType, EventSource eventSource) {
        this.eventType = eventType;
        this.eventSource = eventSource;
    }

    /**
     * Returns the type of movement for this event.
     * 
     * @return the event type
     */
    public EventType getEventType() {
        return eventType;
    }

    /**
     * Returns the source that triggered this event.
     * 
     * @return the event source (USER or THREAD)
     */
    public EventSource getEventSource() {
        return eventSource;
    }
}
