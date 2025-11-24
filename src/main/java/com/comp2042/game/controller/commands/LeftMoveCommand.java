package com.comp2042.game.controller.commands;

import com.comp2042.game.board.Board;
import com.comp2042.game.data.ViewData;

/**
 * Command for moving a brick left.
 * Encapsulates left movement logic following Command pattern.
 */
public class LeftMoveCommand implements MoveCommand {
    private final Board board;
    
    /**
     * Creates a new left move command.
     * 
     * @param board the board to perform the move on
     */
    public LeftMoveCommand(Board board) {
        this.board = board;
    }
    
    /**
     * Executes the left movement command.
     * 
     * @return ViewData containing updated brick position and shape
     */
    @Override
    public ViewData execute() {
        board.moveBrickLeft();
        return board.getViewData();
    }
}

