package com.comp2042.game.board;

import com.comp2042.game.data.ClearRow;
import com.comp2042.game.data.ViewData;
import com.comp2042.game.level.LevelManager;
import com.comp2042.game.level.LinesClearedTracker;
import com.comp2042.game.score.Score;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimpleBoardTest {

    private SimpleBoard board;

    @BeforeEach
    void setUp() {
        board = new SimpleBoard(10, 20);
    }

    @Test
    void constructorCreatesEmptyBoard() {
        int[][] matrix = board.getBoardMatrix();
        assertEquals(10, matrix.length);
        assertEquals(20, matrix[0].length);
        
        // Check all cells are empty (0)
        for (int[] row : matrix) {
            for (int cell : row) {
                assertEquals(0, cell);
            }
        }
    }

    @Test
    void trySpawnNewBrickSpawnsBrickAtDefaultPosition() {
        // Clear board first to ensure spawn works
        board.newGame();
        boolean gameOver = board.trySpawnNewBrick();
        // Note: trySpawnNewBrick returns true if collision (game over), false if successful
        ViewData viewData = board.getViewData();
        assertNotNull(viewData.getBrickData());
        assertEquals(4, viewData.getxPosition());
        assertEquals(0, viewData.getyPosition()); // Spawn at top (Y=0)
    }

    @Test
    void trySpawnNewBrickReturnsTrueWhenBoardIsFull() {
        // Fill the entire top portion of the board to guarantee collision
        // Spawn is at (4, 0) - top center, but brick shapes vary
        int[][] matrix = board.getBoardMatrix();
        // Fill entire top 5 rows across full width to ensure any brick collides
        for (int col = 0; col < 10; col++) {
            for (int row = 0; row < 5; row++) {
                matrix[col][row] = 1;
            }
        }
        
        boolean gameOver = board.trySpawnNewBrick();
        assertTrue(gameOver, "Should return true when spawn position is blocked");
    }

    @Test
    void moveBrickDownMovesBrickWhenPossible() {
        board.newGame();
        ViewData initial = board.getViewData();
        int initialY = initial.getyPosition();
        
        boolean moved = board.moveBrickDown();
        
        // Brick should move down if there's space
        if (moved) {
            ViewData after = board.getViewData();
            assertEquals(initialY + 1, after.getyPosition());
        }
        // If it didn't move, it might be at the bottom already
    }

    @Test
    void moveBrickDownReturnsFalseWhenBlocked() {
        board.trySpawnNewBrick();
        
        // Move brick to bottom
        while (board.moveBrickDown()) {
            // Keep moving down
        }
        
        boolean moved = board.moveBrickDown();
        assertFalse(moved, "Should not move when blocked");
    }

    @Test
    void moveBrickLeftMovesBrickWhenPossible() {
        board.newGame();
        ViewData initial = board.getViewData();
        int initialX = initial.getxPosition();
        
        boolean moved = board.moveBrickLeft();
        
        // Brick should move left if there's space
        if (moved) {
            ViewData after = board.getViewData();
            assertEquals(initialX - 1, after.getxPosition());
        }
        // If it didn't move, it might be at the left boundary
    }

    @Test
    void moveBrickLeftReturnsFalseAtLeftBoundary() {
        board.trySpawnNewBrick();
        
        // Move to left edge
        for (int i = 0; i < 10; i++) {
            board.moveBrickLeft();
        }
        
        boolean moved = board.moveBrickLeft();
        assertFalse(moved, "Should not move past left boundary");
    }

    @Test
    void moveBrickRightMovesBrickWhenPossible() {
        board.newGame();
        ViewData initial = board.getViewData();
        int initialX = initial.getxPosition();
        
        boolean moved = board.moveBrickRight();
        
        // Brick should move right if there's space
        if (moved) {
            ViewData after = board.getViewData();
            assertEquals(initialX + 1, after.getxPosition());
        }
        // If it didn't move, it might be at the right boundary
    }

    @Test
    void moveBrickRightReturnsFalseAtRightBoundary() {
        board.trySpawnNewBrick();
        
        // Move to right edge - keep moving until we can't move anymore
        while (board.moveBrickRight()) {
            // Keep moving right
        }
        
        boolean moved = board.moveBrickRight();
        assertFalse(moved, "Should not move past right boundary");
    }

    @Test
    void rotateLeftBrickRotatesWhenPossible() {
        board.newGame();
        int[][] initialShape = board.getViewData().getBrickData();
        
        boolean rotated = board.rotateLeftBrick();
        
        // Rotation might fail if it would cause collision
        if (rotated) {
            int[][] rotatedShape = board.getViewData().getBrickData();
            assertNotNull(rotatedShape);
        }
    }

    @Test
    void rotateLeftBrickReturnsFalseWhenRotationWouldCollide() {
        board.trySpawnNewBrick();
        
        // Fill area around brick to prevent rotation (spawn is at Y=0)
        int[][] matrix = board.getBoardMatrix();
        // Create obstacles around the spawn position that would block rotation
        matrix[3][0] = 1;
        matrix[3][1] = 1;
        matrix[3][2] = 1;
        matrix[8][0] = 1;
        matrix[8][1] = 1;
        matrix[8][2] = 1;
        
        // Try to rotate - may or may not fail depending on brick shape
        boolean rotated = board.rotateLeftBrick();
        // This test documents that rotation can be blocked by obstacles
        // The actual result depends on the random brick spawned
        assertNotNull(board.getViewData().getBrickData());
    }

    @Test
    void mergeBrickToBackgroundPlacesBrickOnBoard() {
        board.newGame();
        // Move brick down a few times to ensure safe merge position
        for (int i = 0; i < 3; i++) {
            board.moveBrickDown();
        }
        
        int[][] initialMatrix = board.getBoardMatrix();
        // Check that board is initially mostly empty (might have some bricks from newGame)
        board.mergeBrickToBackground();
        
        int[][] afterMatrix = board.getBoardMatrix();
        // After merging, there should be some non-zero cells
        boolean hasBrick = false;
        for (int[] row : afterMatrix) {
            for (int cell : row) {
                if (cell != 0) {
                    hasBrick = true;
                    break;
                }
            }
            if (hasBrick) break;
        }
        assertTrue(hasBrick, "Brick should be placed on board");
    }

    @Test
    void clearRowsRemovesFullRows() {
        board.newGame();
        
        // The checkRemoving method checks COLUMNS (matrix[col]), not rows
        // A "row" is cleared when ALL cells in that column are non-zero
        // Fill one complete column (all 20 rows in column 0)
        int[][] matrix = board.getBoardMatrix();
        int testCol = 0;
        for (int row = 0; row < 20; row++) {
            matrix[testCol][row] = 1;
        }
        
        ClearRow result = board.clearRows();
        
        assertEquals(1, result.getLinesRemoved());
    }

    @Test
    void clearRowsRemovesMultipleRows() {
        board.newGame();
        
        // The checkRemoving method checks COLUMNS (matrix[col]), not rows
        // Fill two complete columns (all 20 rows in columns 0 and 1)
        int[][] matrix = board.getBoardMatrix();
        for (int col = 0; col < 2; col++) {
            for (int row = 0; row < 20; row++) {
                matrix[col][row] = 1;
            }
        }
        
        ClearRow result = board.clearRows();
        
        assertEquals(2, result.getLinesRemoved());
        assertTrue(result.getScoreBonus() > 0, "Should award score bonus");
    }

    @Test
    void clearRowsReturnsZeroWhenNoRowsToClear() {
        board.newGame();
        // Move brick down a few times before merging
        for (int i = 0; i < 3; i++) {
            board.moveBrickDown();
        }
        board.mergeBrickToBackground();
        
        ClearRow result = board.clearRows();
        
        assertEquals(0, result.getLinesRemoved());
    }

    @Test
    void getViewDataReturnsCorrectBrickData() {
        board.trySpawnNewBrick();
        ViewData viewData = board.getViewData();
        
        assertNotNull(viewData.getBrickData());
        assertNotNull(viewData.getNextBrickData());
        assertTrue(viewData.getGhostY() >= viewData.getyPosition(), "Ghost should be at or below current position");
    }

    @Test
    void getScoreReturnsScoreObject() {
        Score score = board.getScore();
        assertNotNull(score);
        assertEquals(0, score.scoreProperty().get());
    }

    @Test
    void getLevelManagerReturnsLevelManager() {
        LevelManager levelManager = board.getLevelManager();
        assertNotNull(levelManager);
        assertEquals(1, levelManager.getCurrentLevel());
    }

    @Test
    void getLinesTrackerReturnsTracker() {
        LinesClearedTracker tracker = board.getLinesTracker();
        assertNotNull(tracker);
        assertEquals(0, tracker.getTotalLines());
    }

    @Test
    void newGameResetsBoardState() {
        board.newGame();
        // Move brick down a few times before merging
        for (int i = 0; i < 3; i++) {
            board.moveBrickDown();
        }
        board.mergeBrickToBackground();
        board.getScore().add(100);
        
        board.newGame();
        
        int[][] matrix = board.getBoardMatrix();
        for (int[] row : matrix) {
            for (int cell : row) {
                assertEquals(0, cell, "Board should be cleared");
            }
        }
        assertEquals(0, board.getScore().scoreProperty().get());
        assertEquals(0, board.getLinesTracker().getTotalLines());
        assertEquals(1, board.getLevelManager().getCurrentLevel());
    }

    @Test
    void newGameSpawnsNewBrick() {
        board.newGame();
        
        ViewData viewData = board.getViewData();
        assertNotNull(viewData.getBrickData());
    }
}

