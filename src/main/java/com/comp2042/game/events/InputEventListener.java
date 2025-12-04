package com.comp2042.game.events;

import com.comp2042.game.data.DownData;
import com.comp2042.game.data.ViewData;

/**
 * Listener interface for handling player input events.
 * Implements the Observer pattern - the UI notifies this listener
 * when user input occurs, and the game controller processes the events.
 * 
 * <p>Each method corresponds to a specific input action and returns
 * data needed to update the view.
 */
public interface InputEventListener {

    /**
     * Handles downward movement of the active brick.
     * 
     * @param event the movement event containing source information
     * @return data about cleared rows and updated view state
     */
    DownData onDownEvent(MoveEvent event);

    /**
     * Handles leftward movement of the active brick.
     * 
     * @param event the movement event containing source information
     * @return updated view data for rendering
     */
    ViewData onLeftEvent(MoveEvent event);

    /**
     * Handles rightward movement of the active brick.
     * 
     * @param event the movement event containing source information
     * @return updated view data for rendering
     */
    ViewData onRightEvent(MoveEvent event);

    /**
     * Handles clockwise rotation of the active brick.
     * 
     * @param event the movement event containing source information
     * @return updated view data for rendering
     */
    ViewData onRotateEvent(MoveEvent event);

    /**
     * Handles instant drop of the active brick to the bottom.
     * 
     * @param event the movement event containing source information
     * @return data about cleared rows and updated view state
     */
    DownData onHardDropEvent(MoveEvent event);

    /**
     * Handles holding/swapping of the active brick with the held brick.
     *
     * @param event the movement event containing source information
     * @return updated view data for rendering after the hold operation
     */
    ViewData onHoldEvent(MoveEvent event);

    /**
     * Creates and initializes a new game, resetting all state.
     */
    void createNewGame();
}
