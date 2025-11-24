package com.comp2042.game.controller.commands;

import com.comp2042.game.board.Board;
import com.comp2042.game.data.ViewData;

/**
 * Command for moving a brick right.
 * Encapsulates right movement logic following Command pattern.
 */
public class RightMoveCommand implements MoveCommand {
    private final Board board;
    
    /**
     * Creates a new right move command.
     * 
     * @param board the board to perform the move on
     */
    public RightMoveCommand(Board board) {
        this.board = board;
    }
    
    /**
     * Executes the right movement command.
     * 
     * @return ViewData containing updated brick position and shape
     */
    @Override
    public ViewData execute() {
        board.moveBrickRight();
        return board.getViewData();
    }
}

