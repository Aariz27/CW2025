package com.comp2042.game.controller.commands;

import com.comp2042.game.board.Board;
import com.comp2042.game.data.ViewData;

/**
 * Command for rotating a brick.
 * Encapsulates rotation logic following Command pattern.
 */
public class RotateMoveCommand implements MoveCommand {
    private final Board board;
    
    /**
     * Creates a new rotate move command.
     * 
     * @param board the board to perform the rotation on
     */
    public RotateMoveCommand(Board board) {
        this.board = board;
    }
    
    /**
     * Executes the rotation command.
     * 
     * @return ViewData containing updated brick position and shape
     */
    @Override
    public ViewData execute() {
        board.rotateLeftBrick();
        return board.getViewData();
    }
}

