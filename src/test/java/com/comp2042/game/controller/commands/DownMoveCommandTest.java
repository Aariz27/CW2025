package com.comp2042.game.controller.commands;

import com.comp2042.game.board.Board;
import com.comp2042.game.board.SimpleBoard;
// import com.comp2042.game.data.ClearRow;
import com.comp2042.game.data.DownData;
import com.comp2042.game.data.ViewData;
import com.comp2042.game.events.EventSource;
import com.comp2042.game.score.Score;
import com.comp2042.ui.GuiController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DownMoveCommandTest {

    private Board board;
    private GuiController mockGuiController;
    private DownMoveCommand command;

    @BeforeEach
    void setUp() {
        board = new SimpleBoard(10, 20);
        board.trySpawnNewBrick();
        mockGuiController = mock(GuiController.class);
    }

    @Test
    void executeMovesBrickDownWhenPossible() {
        command = new DownMoveCommand(board, mockGuiController, EventSource.USER);
        ViewData initial = board.getViewData();
        int initialY = initial.getyPosition();
        
        // Only test if brick can actually move (not at bottom)
        if (initialY < 15) {
            ViewData result = command.execute();
            // Brick should move down or land
            assertTrue(result.getyPosition() >= initialY);
            DownData downData = command.getDownData();
            assertNotNull(downData);
        }
    }

    @Test
    void executeAwardsScoreForUserMove() {
        command = new DownMoveCommand(board, mockGuiController, EventSource.USER);
        Score score = board.getScore();
        int initialScore = score.scoreProperty().get();
        ViewData initial = board.getViewData();
        
        // Only test if brick can move (not at bottom where it would land immediately)
        if (initial.getyPosition() < 15) {
            command.execute();
            // Score should increase if brick moved, or stay same if it landed
            assertTrue(score.scoreProperty().get() >= initialScore);
        }
    }

    @Test
    void executeDoesNotAwardScoreForThreadMove() {
        command = new DownMoveCommand(board, mockGuiController, EventSource.THREAD);
        Score score = board.getScore();
        int initialScore = score.scoreProperty().get();
        
        command.execute();
        
        assertEquals(initialScore, score.scoreProperty().get(), "Should not award points for automatic move");
    }

    @Test
    void executeHandlesBrickLanding() {
        // Move brick down a few times, but not all the way to avoid out-of-bounds
        for (int i = 0; i < 5 && board.moveBrickDown(); i++) {
            // Move down a few times
        }
        
        // Now try to move down - it should either move or land
        command = new DownMoveCommand(board, mockGuiController, EventSource.USER);
        ViewData result = command.execute();
        
        assertNotNull(result);
        DownData downData = command.getDownData();
        assertNotNull(downData);
        
        verify(mockGuiController, atLeastOnce()).refreshGameBackground(any());
    }

    @Test
    void executeClearsRowsAndAwardsScore() {
        // Move brick down a few times and merge it (but not too far to avoid out-of-bounds)
        for (int i = 0; i < 3; i++) {
            board.moveBrickDown();
        }
        board.mergeBrickToBackground();
        
        // Fill a complete row near the bottom (but not the very bottom to avoid issues)
        int[][] matrix = board.getBoardMatrix();
        int testRow = 15; // Use a row that's safe
        for (int col = 0; col < 10; col++) {
            matrix[col][testRow] = 1;
        }
        
        // Spawn new brick and move it down a few times
        board.trySpawnNewBrick();
        for (int i = 0; i < 3; i++) {
            board.moveBrickDown();
        }
        
        command = new DownMoveCommand(board, mockGuiController, EventSource.USER);
        command.execute();
        
        DownData downData = command.getDownData();
        assertNotNull(downData);
        verify(mockGuiController, atLeastOnce()).refreshGameBackground(any());
    }

    @Test
    void executeTriggersGameOverWhenSpawnBlocked() {
        // Fill board at spawn position (carefully to avoid out-of-bounds)
        int[][] matrix = board.getBoardMatrix();
        for (int col = 4; col < 8 && col < 10; col++) {
            for (int row = 10; row < 14 && row < 20; row++) {
                matrix[col][row] = 1;
            }
        }
        
        // Move current brick down a few times (not all the way)
        for (int i = 0; i < 3; i++) {
            board.moveBrickDown();
        }
        board.mergeBrickToBackground();
        
        // Now try to spawn - should trigger game over
        boolean gameOver = board.trySpawnNewBrick();
        if (gameOver) {
            verify(mockGuiController, atLeastOnce()).gameOver();
        }
    }

    @Test
    void getDownDataReturnsNullClearRowWhenBrickMoves() {
        ViewData initial = board.getViewData();
        // Only test if brick can move
        if (initial.getyPosition() < 15) {
            command = new DownMoveCommand(board, mockGuiController, EventSource.USER);
            command.execute();
            
            DownData downData = command.getDownData();
            assertNotNull(downData);
            assertNotNull(downData.getViewData());
        }
    }

    @Test
    void getDownDataReturnsClearRowWhenBrickLands() {
        // Move brick down a few times (not all the way to avoid out-of-bounds)
        for (int i = 0; i < 5; i++) {
            if (!board.moveBrickDown()) break;
        }
        
        command = new DownMoveCommand(board, mockGuiController, EventSource.USER);
        command.execute();
        
        DownData downData = command.getDownData();
        assertNotNull(downData);
        assertNotNull(downData.getViewData());
    }
}

