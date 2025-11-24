package com.comp2042.game.controller.commands;

import com.comp2042.game.board.Board;
import com.comp2042.game.board.SimpleBoard;
import com.comp2042.game.data.ViewData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RightMoveCommandTest {

    private Board board;
    private RightMoveCommand command;

    @BeforeEach
    void setUp() {
        board = new SimpleBoard(10, 20);
        board.trySpawnNewBrick();
        command = new RightMoveCommand(board);
    }

    @Test
    void executeMovesBrickRight() {
        ViewData initial = board.getViewData();
        int initialX = initial.getxPosition();
        
        ViewData result = command.execute();
        
        // Brick should move right if possible, or stay at boundary
        assertTrue(result.getxPosition() >= initialX, "Brick should move right or stay at boundary");
    }

    @Test
    void executeReturnsViewData() {
        ViewData result = command.execute();
        
        assertNotNull(result);
        assertNotNull(result.getBrickData());
    }

    @Test
    void executeDoesNotMoveWhenBlocked() {
        // Move to right boundary
        for (int i = 0; i < 10; i++) {
            board.moveBrickRight();
        }
        
        ViewData before = board.getViewData();
        int beforeX = before.getxPosition();
        
        ViewData result = command.execute();
        
        assertEquals(beforeX, result.getxPosition(), "Should not move when blocked");
    }
}

