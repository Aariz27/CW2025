package com.comp2042.game.controller;

import com.comp2042.game.events.InputEventListener;
import com.comp2042.game.events.MoveEvent;
import com.comp2042.game.data.DownData;
import com.comp2042.game.data.UndoData;
import com.comp2042.game.data.ViewData;
import com.comp2042.game.board.Board;
import com.comp2042.game.board.SimpleBoard;
import com.comp2042.game.controller.commands.*;
import com.comp2042.game.level.LevelManager;
import com.comp2042.ui.GuiController;
import com.comp2042.game.data.BoardStateSnapshot;
import com.comp2042.game.events.EventSource;

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
    private BoardStateSnapshot lastMoveSnapshot;
    private int timeSlowUses = 0;
    private int undoUses = 0;
    
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
        captureSnapshotIfUser(event);
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
        captureSnapshotIfUser(event);
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
        captureSnapshotIfUser(event);
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
        captureSnapshotIfUser(event);
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
        captureSnapshotIfUser(event);
        MoveCommand command = new HardDropMoveCommand(board, viewGuiController);
        command.execute();
        return ((HardDropMoveCommand) command).getDownData();
    }
    
    /**
     * Handles hold event for swapping the current brick with the held brick.
     * If no brick is held, stores the current brick and spawns a new one.
     * If a brick is already held, swaps them.
     *
     * @param event the move event
     * @return ViewData containing updated brick information after the hold operation
     */
    @Override
    public ViewData onHoldEvent(MoveEvent event) {
        captureSnapshotIfUser(event);
        if (board instanceof SimpleBoard simpleBoard) {
            return simpleBoard.holdBrick();
        }
        // Fallback - return current view data if board doesn't support hold
        return board.getViewData();
    }
    
    /**
     * Resets the game to start a new game.
     */
    @Override
    public void createNewGame() {
        board.newGame();
        lastMoveSnapshot = null;
        timeSlowUses = 0;
        undoUses = 0;
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
    }

    /**
     * Checks if the time slow ability can currently be used.
     *
     * @return true if available, otherwise false
     */
    public boolean canUseTimeSlowAbility() {
        return (getTotalLinesCleared() / 3) > timeSlowUses;
    }

    /**
     * Marks a time slow charge as used if one is available.
     *
     * @return true if a charge was consumed
     */
    public boolean consumeTimeSlowCharge() {
        if (canUseTimeSlowAbility()) {
            timeSlowUses++;
            return true;
        }
        return false;
    }

    /**
     * Checks if undo can be performed right now.
     *
     * @return true if undo is available
     */
    public boolean canUseUndoAbility() {
        return (getTotalLinesCleared() / 5) > undoUses && lastMoveSnapshot != null;
    }

    /**
     * Performs an undo, restoring the previous board snapshot when possible.
     *
     * @return UndoData describing the result
     */
    public UndoData undoLastMove() {
        if (!(board instanceof SimpleBoard simpleBoard)) {
            return new UndoData(false, board.getViewData(), board.getBoardMatrix());
        }
        if (!canUseUndoAbility()) {
            return new UndoData(false, board.getViewData(), board.getBoardMatrix());
        }

        undoUses++;
        ViewData viewData = simpleBoard.restoreSnapshot(lastMoveSnapshot);
        lastMoveSnapshot = null;
        return new UndoData(true, viewData, board.getBoardMatrix());
    }

    /**
     * Captures a snapshot of the board state before a user-initiated move.
     * Used to enable undo functionality.
     *
     * @param event the move event that may trigger a snapshot
     */
    private void captureSnapshotIfUser(MoveEvent event) {
        if (event.getEventSource() == EventSource.USER && board instanceof SimpleBoard simpleBoard) {
            lastMoveSnapshot = simpleBoard.createSnapshot();
        }
    }

    /**
     * Gets the total number of cleared lines from the board, if available.
     *
     * @return total cleared lines or 0 when unavailable
     */
    private int getTotalLinesCleared() {
        if (board instanceof SimpleBoard simpleBoard) {
            return simpleBoard.getLinesTracker().getTotalLines();
        }
        return 0;
    }
}
