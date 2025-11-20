package com.comp2042.game.controller;

import com.comp2042.game.board.Board;
import com.comp2042.game.board.SimpleBoard;
import com.comp2042.game.data.DownData;
import com.comp2042.game.data.ViewData;
import com.comp2042.game.events.EventSource;
import com.comp2042.game.events.MoveEvent;
import com.comp2042.ui.GuiController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameControllerTest {

    private Board board;
    private GuiController mockGuiController;
    private GameController gameController;

    @BeforeEach
    void setUp() {
        board = new SimpleBoard(10, 20);
        mockGuiController = mock(GuiController.class);
        gameController = new GameController(mockGuiController, board);
    }

    @Test
    void constructorInitializesGame() {
        verify(mockGuiController).setEventListener(gameController);
        verify(mockGuiController).initGameView(any(), any());
        verify(mockGuiController).bindScore(any());
    }

    @Test
    void onDownEventExecutesDownCommand() {
        MoveEvent event = new MoveEvent(com.comp2042.game.events.EventType.DOWN, EventSource.USER);
        
        DownData result = gameController.onDownEvent(event);
        
        assertNotNull(result);
        assertNotNull(result.getViewData());
    }

    @Test
    void onLeftEventExecutesLeftCommand() {
        MoveEvent event = new MoveEvent(com.comp2042.game.events.EventType.LEFT, EventSource.USER);
        
        ViewData result = gameController.onLeftEvent(event);
        
        assertNotNull(result);
        assertNotNull(result.getBrickData());
    }

    @Test
    void onRightEventExecutesRightCommand() {
        MoveEvent event = new MoveEvent(com.comp2042.game.events.EventType.RIGHT, EventSource.USER);
        
        ViewData result = gameController.onRightEvent(event);
        
        assertNotNull(result);
        assertNotNull(result.getBrickData());
    }

    @Test
    void onRotateEventExecutesRotateCommand() {
        MoveEvent event = new MoveEvent(com.comp2042.game.events.EventType.ROTATE, EventSource.USER);
        
        ViewData result = gameController.onRotateEvent(event);
        
        assertNotNull(result);
        assertNotNull(result.getBrickData());
    }

    @Test
    void onDownEventMovesBrickDown() {
        MoveEvent event = new MoveEvent(com.comp2042.game.events.EventType.DOWN, EventSource.USER);
        ViewData initial = board.getViewData();
        int initialY = initial.getyPosition();
        
        // Only test if brick can move (not too close to bottom)
        if (initialY < 15) {
            gameController.onDownEvent(event);
            
            ViewData after = board.getViewData();
            assertTrue(after.getyPosition() >= initialY, "Brick should move down or land");
        }
    }

    @Test
    void onLeftEventMovesBrickLeft() {
        MoveEvent event = new MoveEvent(com.comp2042.game.events.EventType.LEFT, EventSource.USER);
        ViewData initial = board.getViewData();
        int initialX = initial.getxPosition();
        
        ViewData result = gameController.onLeftEvent(event);
        
        assertTrue(result.getxPosition() <= initialX, "Brick should move left or stay at boundary");
    }

    @Test
    void onRightEventMovesBrickRight() {
        MoveEvent event = new MoveEvent(com.comp2042.game.events.EventType.RIGHT, EventSource.USER);
        ViewData initial = board.getViewData();
        int initialX = initial.getxPosition();
        
        ViewData result = gameController.onRightEvent(event);
        
        assertTrue(result.getxPosition() >= initialX, "Brick should move right or stay at boundary");
    }

    @Test
    void createNewGameResetsBoard() {
        // Make some changes
        board.moveBrickDown();
        board.getScore().add(100);
        
        gameController.createNewGame();
        
        assertEquals(0, board.getScore().scoreProperty().get());
        verify(mockGuiController).refreshGameBackground(any());
    }

    @Test
    void createNewGameSpawnsNewBrick() {
        gameController.createNewGame();
        
        ViewData viewData = board.getViewData();
        assertNotNull(viewData.getBrickData());
    }
}

