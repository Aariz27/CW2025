package com.comp2042.game.events;

/**
 * Enumeration indicating the origin of a game event.
 * Used to distinguish between player-initiated actions and automatic game events.
 */
public enum EventSource {
    /** Event triggered by user input (keyboard) */
    USER,
    
    /** Event triggered automatically by the game timer thread */
    THREAD
}
