package com.comp2042.ui;

import com.comp2042.game.events.EventSource;
import com.comp2042.game.events.EventType;
import com.comp2042.game.events.InputEventListener;
import com.comp2042.game.events.MoveEvent;
import com.comp2042.game.data.ViewData;
import javafx.beans.property.BooleanProperty;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import java.util.function.Consumer;

/**
 * Handles keyboard input for the game.
 * Extracted from GuiController following Single Responsibility Principle -
 * separates input handling from UI rendering and game logic.
 */
public final class InputHandler {

    private final InputEventListener eventListener;
    private final BooleanProperty isPause;
    private final BooleanProperty isGameOver;
    private final Runnable onNewGameRequested;
    private final Runnable onSoftDropRequested;
    private final Runnable onHardDropRequested;
    private final Runnable onPauseRequested;
    private final Consumer<ViewData> onBrickMoved;

    /**
     * Creates a new InputHandler.
     * 
     * @param eventListener the listener for game events
     * @param isPause the pause state property
     * @param isGameOver the game over state property
     * @param onNewGameRequested callback for new game requests
     * @param onSoftDropRequested callback for soft drop requests (Down/S key)
     * @param onHardDropRequested callback for hard drop requests (Space key)
     * @param onPauseRequested callback for pause toggle (P key)
     * @param onBrickMoved callback to refresh brick after movement (accepts ViewData)
     */
    public InputHandler(InputEventListener eventListener,
                        BooleanProperty isPause,
                        BooleanProperty isGameOver,
                        Runnable onNewGameRequested,
                        Runnable onSoftDropRequested,
                        Runnable onHardDropRequested,
                        Runnable onPauseRequested,
                        Consumer<ViewData> onBrickMoved) {
        this.eventListener = eventListener;
        this.isPause = isPause;
        this.isGameOver = isGameOver;
        this.onNewGameRequested = onNewGameRequested;
        this.onSoftDropRequested = onSoftDropRequested;
        this.onHardDropRequested = onHardDropRequested;
        this.onPauseRequested = onPauseRequested;
        this.onBrickMoved = onBrickMoved;
    }

    /**
     * Builds and returns the key event handler.
     * 
     * @return EventHandler for keyboard input
     */
    public EventHandler<KeyEvent> build() {
        return keyEvent -> {
            if (isPause.getValue() == Boolean.FALSE && isGameOver.getValue() == Boolean.FALSE) {
                if (keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.A) {
                    ViewData viewData = eventListener.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER));
                    onBrickMoved.accept(viewData);
                    keyEvent.consume();
                }
                if (keyEvent.getCode() == KeyCode.RIGHT || keyEvent.getCode() == KeyCode.D) {
                    ViewData viewData = eventListener.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER));
                    onBrickMoved.accept(viewData);
                    keyEvent.consume();
                }
                if (keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() == KeyCode.W) {
                    ViewData viewData = eventListener.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER));
                    onBrickMoved.accept(viewData);
                    keyEvent.consume();
                }
                if (keyEvent.getCode() == KeyCode.DOWN || keyEvent.getCode() == KeyCode.S) {
                    onSoftDropRequested.run();
                    keyEvent.consume();
                }
                if (keyEvent.getCode() == KeyCode.SPACE) {
                    onHardDropRequested.run();
                    keyEvent.consume();
                }
            }
            if (keyEvent.getCode() == KeyCode.N) {
                onNewGameRequested.run();
                keyEvent.consume();
            }
            if (keyEvent.getCode() == KeyCode.P) {
                onPauseRequested.run();
                keyEvent.consume();
            }
        };
    }
}