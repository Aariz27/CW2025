package com.comp2042.game.events;

/**
 * Enumeration of all possible player input event types in the game.
 * Each event type corresponds to a specific brick movement or rotation action.
 */
public enum EventType {
    /** Move the active brick down by one row */
    DOWN,
    
    /** Move the active brick left by one column */
    LEFT,
    
    /** Move the active brick right by one column */
    RIGHT,
    
    /** Rotate the active brick clockwise */
    ROTATE,
    
    /** Instantly drop the active brick to the lowest possible position */
    HARD_DROP,

    /** Hold/swap the current brick with the held brick */
    HOLD
}
