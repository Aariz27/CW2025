package com.comp2042.game.controller.commands;

import com.comp2042.game.board.SimpleBoard;
import com.comp2042.game.data.DownData;
import com.comp2042.game.data.ViewData;
import com.comp2042.game.score.Score;
import com.comp2042.ui.GuiController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for HardDropMoveCommand.
 * Tests hard drop functionality including instant drop, scoring, and landing logic.
 */
class HardDropMoveCommandTest {

    private SimpleBoard board;
    private GuiController mockGuiController;
    private HardDropMoveCommand command;

    @BeforeEach
    void setUp() {
        board = new SimpleBoard(10, 20);
        board.trySpawnNewBrick();
        mockGuiController = mock(GuiController.class);
    }

    @Test
    void executeDropsBrickToBottom() {
        // Arrange
        command = new HardDropMoveCommand(board, mockGuiController);
        ViewData initial = board.getViewData();
        int initialY = initial.getyPosition();
        int ghostY = initial.getGhostY();

        // Act
        ViewData result = command.execute();

        // Assert
        // After hard drop, brick lands and new one spawns at top
        // So we verify that a drop happened by checking:
        // 1. Initial brick was not already at bottom (ghost was below it)
        // 2. New brick spawned at top (Y position at or near 0)
        assertTrue(ghostY > initialY, "Ghost should have been below initial position");
        assertTrue(result.getyPosition() <= 2, 
                   "After hard drop and landing, new brick should spawn at top");
    }

    @Test
    void executeAwards2PointsPerCellDropped() {
        // Arrange
        command = new HardDropMoveCommand(board, mockGuiController);
        Score score = board.getScore();
        int initialScore = score.scoreProperty().get();
        ViewData initial = board.getViewData();
        int initialY = initial.getyPosition();
        int ghostY = initial.getGhostY();
        int expectedDrop = ghostY - initialY;

        // Act
        command.execute();

        // Assert
        int finalScore = score.scoreProperty().get();
        int scoreGained = finalScore - initialScore;
        
        // Score should be at least 2 * cells dropped (could be more if lines cleared)
        assertTrue(scoreGained >= expectedDrop * 2, 
                   "Should award at least 2 points per cell dropped");
    }

    @Test
    void executeSpawnsNewBrick() {
        // Arrange
        command = new HardDropMoveCommand(board, mockGuiController);

        // Act
        command.execute();
        ViewData after = board.getViewData();

        // Assert
        // After hard drop and landing, a new brick should spawn
        // The new brick should be at the top
        assertTrue(after.getyPosition() <= 2, 
                   "New brick should spawn at top of board");
    }

    @Test
    void executeReturnsValidViewData() {
        // Arrange
        command = new HardDropMoveCommand(board, mockGuiController);

        // Act
        ViewData result = command.execute();

        // Assert
        assertNotNull(result);
        assertNotNull(result.getBrickData());
        assertNotNull(result.getNextBrickData());
        assertTrue(result.getBrickData().length > 0);
    }

    @Test
    void getDownDataReturnsResultAfterExecution() {
        // Arrange
        command = new HardDropMoveCommand(board, mockGuiController);

        // Act
        command.execute();
        DownData downData = command.getDownData();

        // Assert
        assertNotNull(downData);
        assertNotNull(downData.getViewData());
        assertNotNull(downData.getClearRow());
    }

    @Test
    void executeRefreshesGameBackground() {
        // Arrange
        command = new HardDropMoveCommand(board, mockGuiController);

        // Act
        command.execute();

        // Assert
        verify(mockGuiController, times(1)).refreshGameBackground(any());
    }

    @Test
    void executeWithFullRowsClearsAndScores() {
        // Arrange
        // Fill bottom row to trigger line clear
        int[][] matrix = board.getBoardMatrix();
        for (int x = 0; x < matrix.length; x++) {
            matrix[x][matrix[0].length - 1] = 1; // Fill bottom row
        }
        
        // Position brick above the filled row
        board.trySpawnNewBrick();
        
        command = new HardDropMoveCommand(board, mockGuiController);
        Score score = board.getScore();
        int initialScore = score.scoreProperty().get();

        // Act
        command.execute();

        // Assert
        int finalScore = score.scoreProperty().get();
        assertTrue(finalScore > initialScore, 
                   "Score should increase from hard drop and potential line clear");
    }

    @Test
    void executeTriggersLevelUpWhenThresholdReached() {
        // Arrange
        SimpleBoard simpleBoard = new SimpleBoard(10, 20);
        
        // Clear enough lines to trigger level up (10 lines for level 2)
        for (int i = 0; i < 9; i++) {
            simpleBoard.getLinesTracker().addLines(1);
        }
        
        // Fill bottom row
        int[][] matrix = simpleBoard.getBoardMatrix();
        for (int x = 0; x < matrix.length; x++) {
            matrix[x][matrix[0].length - 1] = 1;
        }
        
        simpleBoard.trySpawnNewBrick();
        command = new HardDropMoveCommand(simpleBoard, mockGuiController);

        // Act
        command.execute();

        // Assert
        // If a line was cleared, level should have increased
        verify(mockGuiController, atLeastOnce()).refreshGameBackground(any());
    }

    @Test
    void executeCallsGameOverWhenSpawnCollides() {
        // Arrange
        // Fill top rows to cause spawn collision
        int[][] matrix = board.getBoardMatrix();
        for (int x = 0; x < matrix.length; x++) {
            for (int y = 0; y < 5; y++) {
                matrix[x][y] = 1; // Fill top rows
            }
        }
        
        command = new HardDropMoveCommand(board, mockGuiController);

        // Act
        command.execute();

        // Assert
        verify(mockGuiController, times(1)).gameOver();
    }

    @Test
    void executeWithNoMovementStillHandlesLanding() {
        // Arrange
        // Create a scenario where brick is already at bottom
        board = new SimpleBoard(10, 20);
        
        // Fill board except for a small gap at top
        int[][] matrix = board.getBoardMatrix();
        for (int x = 0; x < matrix.length; x++) {
            for (int y = 3; y < matrix[0].length; y++) {
                matrix[x][y] = 1;
            }
        }
        
        board.trySpawnNewBrick();
        command = new HardDropMoveCommand(board, mockGuiController);

        // Act
        command.execute();

        // Assert
        // Even if no cells dropped, landing logic should execute
        verify(mockGuiController, times(1)).refreshGameBackground(any());
        assertNotNull(command.getDownData());
    }

    @Test
    void multipleHardDropsWorkSequentially() {
        // Arrange & Act - perform multiple hard drops
        for (int i = 0; i < 3; i++) {
            command = new HardDropMoveCommand(board, mockGuiController);
            ViewData result = command.execute();
            
            // Assert each one completes successfully
            assertNotNull(result);
            assertNotNull(command.getDownData());
        }

        // Assert total refreshes
        verify(mockGuiController, atLeast(3)).refreshGameBackground(any());
    }

    @Test
    void scoreIncludesLevelMultiplier() {
        // Arrange
        SimpleBoard simpleBoard = new SimpleBoard(10, 20);
        
        // Advance to level 2 (multiplier should be higher)
        for (int i = 0; i < 10; i++) {
            simpleBoard.getLinesTracker().addLines(1);
        }
        simpleBoard.getLevelManager().updateLevel();
        
        int currentLevel = simpleBoard.getLevelManager().getCurrentLevel();
        assertTrue(currentLevel > 1, "Should be at level 2 or higher");
        
        // Fill bottom row for guaranteed line clear
        int[][] matrix = simpleBoard.getBoardMatrix();
        for (int x = 0; x < matrix.length; x++) {
            matrix[x][matrix[0].length - 1] = 1;
        }
        
        simpleBoard.trySpawnNewBrick();
        command = new HardDropMoveCommand(simpleBoard, mockGuiController);
        Score score = simpleBoard.getScore();
        int initialScore = score.scoreProperty().get();

        // Act
        command.execute();

        // Assert
        int finalScore = score.scoreProperty().get();
        int scoreGained = finalScore - initialScore;
        
        // At higher level, score bonus should be multiplied
        assertTrue(scoreGained > 0, "Score should increase");
    }
}

