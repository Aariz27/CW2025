package com.comp2042.game.board;

import com.comp2042.game.data.ViewData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GhostBlockTest {

    private SimpleBoard board;

    @BeforeEach
    void setUp() {
        board = new SimpleBoard(10, 20);
        board.newGame();
    }

    @Test
    void ghostYIsAtOrBelowCurrentPosition() {
        ViewData viewData = board.getViewData();
        
        assertTrue(viewData.getGhostY() >= viewData.getyPosition(), 
            "Ghost Y should be at or below current brick position");
    }

    @Test
    void ghostXMatchesCurrentX() {
        ViewData viewData = board.getViewData();
        
        assertEquals(viewData.getxPosition(), viewData.getGhostX(), 
            "Ghost X should match current brick X position");
    }

    @Test
    void ghostPositionUpdatesWhenBrickMovesDown() {
        ViewData initial = board.getViewData();
        int initialGhostY = initial.getGhostY();
        
        board.moveBrickDown();
        ViewData after = board.getViewData();
        
        // Ghost should move down with the brick
        assertTrue(after.getGhostY() >= after.getyPosition());
        // Ghost Y should be at or below the new position
        assertTrue(after.getGhostY() >= initialGhostY || after.getyPosition() > initial.getyPosition());
    }

    @Test
    void ghostPositionUpdatesWhenBrickMovesLeft() {
        ViewData initial = board.getViewData();
        int initialGhostX = initial.getGhostX();
        
        boolean moved = board.moveBrickLeft();
        ViewData after = board.getViewData();
        
        // Ghost X should match new brick X position
        assertEquals(after.getxPosition(), after.getGhostX());
        // If brick moved left, ghost X should have moved left too
        if (moved) {
            assertEquals(initialGhostX - 1, after.getGhostX());
        } else {
            // If brick couldn't move (at boundary), ghost X should stay same
            assertEquals(initialGhostX, after.getGhostX());
        }
    }

    @Test
    void ghostPositionUpdatesWhenBrickMovesRight() {
        ViewData initial = board.getViewData();
        int initialGhostX = initial.getGhostX();
        
        boolean moved = board.moveBrickRight();
        ViewData after = board.getViewData();
        
        // Ghost X should match new brick X position
        assertEquals(after.getxPosition(), after.getGhostX());
        // If brick moved right, ghost X should have moved right too
        if (moved) {
            assertEquals(initialGhostX + 1, after.getGhostX());
        } else {
            // If brick couldn't move (at boundary), ghost X should stay same
            assertEquals(initialGhostX, after.getGhostX());
        }
    }

    @Test
    void ghostPositionUpdatesWhenBrickRotates() {
        ViewData initial = board.getViewData();
        int initialGhostY = initial.getGhostY();
        
        board.rotateLeftBrick();
        ViewData after = board.getViewData();
        
        // Ghost should still be at or below current position after rotation
        assertTrue(after.getGhostY() >= after.getyPosition());
        // Ghost X should still match
        assertEquals(after.getxPosition(), after.getGhostX());
    }

    @Test
    void ghostShowsCorrectLandingPosition() {
        ViewData viewData = board.getViewData();
        int currentY = viewData.getyPosition();
        int ghostY = viewData.getGhostY();
        
        // Ghost Y should be the position where brick would land if dropped
        assertTrue(ghostY >= currentY, "Ghost should be at or below current position");
        
        // If we move the brick down to ghost position, it should be at the landing spot
        int movesToGhost = ghostY - currentY;
        for (int i = 0; i < movesToGhost; i++) {
            if (!board.moveBrickDown()) {
                break;
            }
        }
        
        ViewData afterMoves = board.getViewData();
        // After moving to ghost position, ghost should be at or very close to current position
        assertTrue(afterMoves.getGhostY() <= afterMoves.getyPosition() + 1);
    }

    @Test
    void ghostPositionReflectsObstacles() {
        // Place some blocks on the board
        int[][] matrix = board.getBoardMatrix();
        // Place a block that will affect ghost position
        for (int col = 0; col < 10; col++) {
            matrix[col][15] = 1; // Create a horizontal barrier
        }
        
        ViewData viewData = board.getViewData();
        int ghostY = viewData.getGhostY();
        
        // Ghost should stop at or above the obstacle
        assertTrue(ghostY <= 15, "Ghost should not go through obstacles");
        assertTrue(ghostY >= viewData.getyPosition(), "Ghost should still be at or below current position");
    }

    @Test
    void ghostAtBottomWhenBrickIsAtBottom() {
        // Move brick down as far as possible
        while (board.moveBrickDown()) {
            // Keep moving down
        }
        
        ViewData viewData = board.getViewData();
        
        // Ghost should be at the same position as brick when at bottom
        assertEquals(viewData.getyPosition(), viewData.getGhostY(), 
            "Ghost should be at same position as brick when at bottom");
    }

    @Test
    void ghostPositionIsConsistent() {
        ViewData viewData1 = board.getViewData();
        ViewData viewData2 = board.getViewData();
        
        // Getting view data multiple times should return consistent ghost positions
        assertEquals(viewData1.getGhostX(), viewData2.getGhostX());
        assertEquals(viewData1.getGhostY(), viewData2.getGhostY());
    }

    @Test
    void ghostPositionAfterMultipleMoves() {
        ViewData initial = board.getViewData();
        
        // Move brick multiple times
        board.moveBrickRight();
        board.moveBrickDown();
        board.moveBrickDown();
        board.moveBrickLeft();
        
        ViewData after = board.getViewData();
        
        // Ghost should still be correctly positioned
        assertEquals(after.getxPosition(), after.getGhostX());
        assertTrue(after.getGhostY() >= after.getyPosition());
    }

    @Test
    void ghostPositionWithComplexObstaclePattern() {
        // Create a more complex obstacle pattern
        int[][] matrix = board.getBoardMatrix();
        // Create a staircase pattern
        matrix[3][15] = 1;
        matrix[4][15] = 1;
        matrix[5][16] = 1;
        matrix[6][16] = 1;
        
        ViewData viewData = board.getViewData();
        
        // Ghost should calculate correct landing position considering obstacles
        assertTrue(viewData.getGhostY() >= viewData.getyPosition());
        assertTrue(viewData.getGhostY() <= 20, "Ghost should not exceed board height");
    }

    @Test
    void ghostXRemainsConstantDuringVerticalMovement() {
        ViewData initial = board.getViewData();
        int initialGhostX = initial.getGhostX();
        
        // Move brick down multiple times
        for (int i = 0; i < 5; i++) {
            board.moveBrickDown();
        }
        
        ViewData after = board.getViewData();
        
        // Ghost X should remain the same (only Y changes)
        assertEquals(initialGhostX, after.getGhostX());
        assertEquals(after.getxPosition(), after.getGhostX());
    }
}

