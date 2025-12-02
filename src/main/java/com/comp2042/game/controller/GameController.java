package com.comp2042.game.controller;

import com.comp2042.game.events.InputEventListener;
import com.comp2042.game.events.MoveEvent;
import com.comp2042.game.data.DownData;
import com.comp2042.game.data.ViewData;
import com.comp2042.game.board.Board;
import com.comp2042.game.board.SimpleBoard;
import com.comp2042.game.controller.commands.*;
import com.comp2042.game.level.LevelManager;
import com.comp2042.ui.GuiController;

/**
 * Coordinates game logic and user input.
 * Uses Command pattern to eliminate nested conditionals - each move operation
 * is encapsulated in a command object, allowing polymorphic execution.
 * 
 * Dependency Injection: Accepts Board interface (not concrete SimpleBoard),
 * following Dependency Inversion Principle for better testability.
 */
public class GameController implements InputEventListener {
    private final GuiController viewGuiController;
    private final Board board; // Depends on interface, not concrete class
    
    /**
     * Creates a new GameController with dependency injection.
     * Accepts Board interface rather than concrete SimpleBoard, following
     * Dependency Inversion Principle for better testability.
     * 
     * @param c the GUI controller for UI updates
     * @param board the board implementation (injected via interface)
     */
    public GameController(GuiController c, Board board) {
        this.board = board;
        viewGuiController = c;
        board.trySpawnNewBrick();
        viewGuiController.setEventListener(this);
        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());
        viewGuiController.bindScore(board.getScore().scoreProperty());
        
        // Bind level and high score if board is SimpleBoard
        if (board instanceof SimpleBoard simpleBoard) {
            LevelManager levelManager = simpleBoard.getLevelManager();
            viewGuiController.bindLevel(levelManager);
            viewGuiController.bindHighScore(simpleBoard.getHighScoreManager());
        }
    }
    
    /**
     * Handles down movement event using Command pattern.
     * Encapsulates complex down movement logic in a command object.
     * 
     * @param event the move event
     * @return DownData containing clear row information and view data
     */
    @Override
    public DownData onDownEvent(MoveEvent event) {
        MoveCommand command = new DownMoveCommand(board, viewGuiController, event.getEventSource());
        command.execute();
        return ((DownMoveCommand) command).getDownData();
    }
    
    /**
     * Handles left movement event using Command pattern.
     * Polymorphic command execution allows new move types to be added
     * without modifying this class (Open/Closed Principle).
     * 
     * @param event the move event
     * @return ViewData containing updated brick position and shape
     */
    @Override
    public ViewData onLeftEvent(MoveEvent event) {
        MoveCommand command = new LeftMoveCommand(board);
        return command.execute();
    }
    
    /**
     * Handles right movement event using Command pattern.
     * 
     * @param event the move event
     * @return ViewData containing updated brick position and shape
     */
    @Override
    public ViewData onRightEvent(MoveEvent event) {
        MoveCommand command = new RightMoveCommand(board);
        return command.execute();
    }
    
    /**
     * Handles rotation event using Command pattern.
     * 
     * @param event the move event
     * @return ViewData containing updated brick position and shape
     */
    @Override
    public ViewData onRotateEvent(MoveEvent event) {
        MoveCommand command = new RotateMoveCommand(board);
        return command.execute();
    }
    
    /**
     * Handles hard drop event using Command pattern.
     * Instantly drops the piece to the ghost position (where it will land).
     * Awards 2 points per cell dropped and handles landing logic.
     * 
     * @param event the move event
     * @return DownData containing clear row information and view data
     */
    @Override
    public DownData onHardDropEvent(MoveEvent event) {
        MoveCommand command = new HardDropMoveCommand(board, viewGuiController);
        command.execute();
        return ((HardDropMoveCommand) command).getDownData();
    }
    
    /**
     * Resets the game to start a new game.
     */
    @Override
    public void createNewGame() {
        board.newGame();
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
    }
}
