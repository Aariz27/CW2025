package com.comp2042.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 * Panel displayed when the game ends.
 * Shows "GAME OVER" message and provides a "New Game" button.
 */
public class GameOverPanel extends BorderPane {

    private final Button newGameButton;

    public GameOverPanel() {
        final Label gameOverLabel = new Label("GAME OVER");
        gameOverLabel.getStyleClass().add("gameOverStyle");
        
        newGameButton = new Button("New Game (N)");
        newGameButton.getStyleClass().add("game-button");
        
        VBox content = new VBox(10);
        content.setAlignment(Pos.CENTER);
        content.getChildren().addAll(gameOverLabel, newGameButton);
        
        setCenter(content);
    }
    
    /**
     * Gets the new game button for event binding.
     * 
     * @return the new game button
     */
    public Button getNewGameButton() {
        return newGameButton;
    }

}
