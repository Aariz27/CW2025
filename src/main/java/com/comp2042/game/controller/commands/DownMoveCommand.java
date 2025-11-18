package com.comp2042.game.controller.commands;

import com.comp2042.game.board.Board;
import com.comp2042.game.board.SimpleBoard;
import com.comp2042.game.data.DownData;
import com.comp2042.game.data.ViewData;
import com.comp2042.game.events.EventSource;
import com.comp2042.game.level.LevelManager;
import com.comp2042.ui.GuiController;

/**
 * Command for moving a brick down.
 * Encapsulates the complex down movement logic (landing, row clearing, scoring)
 * that was previously nested conditionals in GameController.
 */
public class DownMoveCommand implements MoveCommand {
    private final Board board;
    private final GuiController guiController;
    private final EventSource eventSource;
    private DownData result;
    
    /**
     * Creates a new down move command.
     * 
     * @param board the board to perform the move on
     * @param guiController the GUI controller for updating the display
     * @param eventSource the source of the move event (USER or THREAD)
     */
    public DownMoveCommand(Board board, GuiController guiController, EventSource eventSource) {
        this.board = board;
        this.guiController = guiController;
        this.eventSource = eventSource;
    }
    
    /**
     * Executes the down movement command.
     * Handles both successful movement and brick landing scenarios.
     * 
     * @return ViewData containing updated brick position and shape
     */
    @Override
    public ViewData execute() {
        boolean canMove = board.moveBrickDown();
        if (!canMove) {
            handleBrickLanded();
        } else {
            handleBrickMoved();
        }
        return board.getViewData();
    }
    
    /**
     * Gets the DownData result after command execution.
     * 
     * @return the DownData containing clear row information and view data
     */
    public DownData getDownData() {
        return result;
    }
    
    /**
     * Handles the case when a brick cannot move down (lands).
     * Encapsulates landing logic: merge brick to background, clear rows,
     * update score with level multiplier, track lines cleared, and spawn new brick.
     */
    private void handleBrickLanded() {
        board.mergeBrickToBackground();
        var clearRow = board.clearRows();
        if (clearRow.getLinesRemoved() > 0) {
            // Get level manager if board is SimpleBoard
            LevelManager levelManager = null;
            if (board instanceof SimpleBoard simpleBoard) {
                levelManager = simpleBoard.getLevelManager();
                // Track lines cleared
                simpleBoard.getLinesTracker().addLines(clearRow.getLinesRemoved());
                // Update level based on new total
                boolean levelUp = levelManager.updateLevel();
                if (levelUp) {
                    guiController.onLevelUp(levelManager.getCurrentLevel());
                }
                // Apply level multiplier to score
                int baseScore = clearRow.getScoreBonus();
                double multiplier = levelManager.getCurrentLevelConfig().getScoreMultiplier();
                int finalScore = (int) (baseScore * multiplier);
                board.getScore().add(finalScore);
            } else {
                // Fallback if not SimpleBoard (shouldn't happen in practice)
                board.getScore().add(clearRow.getScoreBonus());
            }
        }
        if (board.trySpawnNewBrick()) {
            guiController.gameOver();
        }
        guiController.refreshGameBackground(board.getBoardMatrix());
        result = new DownData(clearRow, board.getViewData());
    }
    
    /**
     * Handles the case when a brick successfully moves down.
     * Only awards points for user-initiated moves, not automatic drops.
     */
    private void handleBrickMoved() {
        if (eventSource == EventSource.USER) {
            board.getScore().add(1);
        }
        result = new DownData(null, board.getViewData());
    }
}

