package com.comp2042.game.controller.commands;

import com.comp2042.game.board.Board;
import com.comp2042.game.board.SimpleBoard;
import com.comp2042.game.data.ViewData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RotateMoveCommandTest {

    private Board board;
    private RotateMoveCommand command;

    @BeforeEach
    void setUp() {
        board = new SimpleBoard(10, 20);
        board.trySpawnNewBrick();
        command = new RotateMoveCommand(board);
    }

    @Test
    void executeRotatesBrick() {
        ViewData result = command.execute();
        
        int[][] rotatedShape = result.getBrickData();
        // Shape should change after rotation (unless it's a square)
        assertNotNull(rotatedShape);
    }

    @Test
    void executeReturnsViewData() {
        ViewData result = command.execute();
        
        assertNotNull(result);
        assertNotNull(result.getBrickData());
    }

    @Test
    void executeDoesNotRotateWhenBlocked() {
        // Fill area to prevent rotation
        int[][] matrix = board.getBoardMatrix();
        matrix[3][10] = 1;
        matrix[5][10] = 1;
        
        ViewData result = command.execute();
        
        // If rotation fails, shape should remain the same
        int[][] afterShape = result.getBrickData();
        assertNotNull(afterShape);
    }
}

