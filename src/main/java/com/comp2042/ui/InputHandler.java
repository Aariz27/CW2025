package com.comp2042.ui;

import com.comp2042.game.events.EventSource;
import com.comp2042.game.events.EventType;
import com.comp2042.game.events.InputEventListener;
import com.comp2042.game.events.MoveEvent;
import javafx.beans.property.BooleanProperty;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public final class InputHandler {

    private final InputEventListener eventListener;
    private final BooleanProperty isPause;
    private final BooleanProperty isGameOver;
    private final Runnable onNewGameRequested;
    private final Runnable onSoftDropRequested;

    public InputHandler(InputEventListener eventListener,
                        BooleanProperty isPause,
                        BooleanProperty isGameOver,
                        Runnable onNewGameRequested,
                        Runnable onSoftDropRequested) {
        this.eventListener = eventListener;
        this.isPause = isPause;
        this.isGameOver = isGameOver;
        this.onNewGameRequested = onNewGameRequested;
        this.onSoftDropRequested = onSoftDropRequested;
    }

    public EventHandler<KeyEvent> build() {
        return keyEvent -> {
            if (isPause.getValue() == Boolean.FALSE && isGameOver.getValue() == Boolean.FALSE) {
                if (keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.A) {
                    eventListener.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER));
                    keyEvent.consume();
                }
                if (keyEvent.getCode() == KeyCode.RIGHT || keyEvent.getCode() == KeyCode.D) {
                    eventListener.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER));
                    keyEvent.consume();
                }
                if (keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() == KeyCode.W) {
                    eventListener.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER));
                    keyEvent.consume();
                }
                if (keyEvent.getCode() == KeyCode.DOWN || keyEvent.getCode() == KeyCode.S) {
                    onSoftDropRequested.run();
                    keyEvent.consume();
                }
            }
            if (keyEvent.getCode() == KeyCode.N) {
                onNewGameRequested.run();
                keyEvent.consume();
            }
        };
    }
}