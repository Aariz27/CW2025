package com.comp2042.game.controller.commands;

import com.comp2042.game.board.Board;
import com.comp2042.game.board.SimpleBoard;
import com.comp2042.game.data.DownData;
import com.comp2042.game.data.ViewData;
import com.comp2042.game.level.LevelManager;
import com.comp2042.ui.GuiController;

/**
 * Command for hard dropping a brick instantly to the landing position (ghost position).
 * Moves the brick down repeatedly until it lands, awards 2 points per cell dropped,
 * and handles landing logic (clearing rows, spawning new brick).
 * 
 * Follows Command pattern - encapsulates hard drop operation as an object
 * for polymorphic execution alongside other move commands.
 */
public class HardDropMoveCommand implements MoveCommand {
    private final Board board;
    private final GuiController guiController;
    private DownData result;
    
    /**
     * Creates a new hard drop command.
     * 
     * @param board the board to perform the move on
     * @param guiController the GUI controller for updating the display
     */
    public HardDropMoveCommand(Board board, GuiController guiController) {
        this.board = board;
        this.guiController = guiController;
    }
    
    /**
     * Executes the hard drop command.
     * Moves brick down until it lands at ghost position, awards 2 points per cell dropped.
     * 
     * @return ViewData containing updated brick position and shape after landing
     */
    @Override
    public ViewData execute() {
        int cellsDropped = 0;
        
        // Keep moving down until brick can't move anymore (reaches ghost position)
        while (board.moveBrickDown()) {
            cellsDropped++;
        }
        
        // Award points for hard drop (2 points per cell dropped)
        if (cellsDropped > 0) {
            board.getScore().add(cellsDropped * 2);
        }
        
        // Handle landing: merge, clear rows, spawn new brick
        handleBrickLanded();
        
        return board.getViewData();
    }
    
    /**
     * Gets the DownData result after command execution.
     * Contains information about cleared rows and updated view state.
     * 
     * @return the DownData containing clear row information and view data
     */
    public DownData getDownData() {
        return result;
    }
    
    /**
     * Handles the brick landing after hard drop.
     * Merges brick to background, clears completed rows, updates score with level multiplier,
     * tracks lines cleared, checks for level up, and spawns new brick.
     * Encapsulates complex landing logic to maintain Single Responsibility Principle.
     */
    private void handleBrickLanded() {
        board.mergeBrickToBackground();
        var clearRow = board.clearRows();
        
        if (clearRow.getLinesRemoved() > 0) {
            // Get level manager if board is SimpleBoard
            if (board instanceof SimpleBoard simpleBoard) {
                LevelManager levelManager = simpleBoard.getLevelManager();
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
        
        // Spawn new brick and check for game over
        if (board.trySpawnNewBrick()) {
            guiController.gameOver();
        }
        
        guiController.refreshGameBackground(board.getBoardMatrix());
        result = new DownData(clearRow, board.getViewData());
    }
}

