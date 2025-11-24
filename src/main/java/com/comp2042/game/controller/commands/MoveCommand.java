package com.comp2042.game.controller.commands;

import com.comp2042.game.data.ViewData;

/**
 * Command interface for move operations.
 * Encapsulates move logic in command objects, replacing nested conditionals
 * with polymorphic execution. New move types can be added without modifying GameController.
 */
public interface MoveCommand {
    /**
     * Executes the move command.
     * 
     * @return ViewData containing updated brick position and shape
     */
    ViewData execute();
}

