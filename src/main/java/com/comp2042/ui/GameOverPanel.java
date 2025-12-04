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

    /** Creates the game over panel with the restart action. */
    public GameOverPanel() {
        final Label gameOverLabel = new Label("GAME OVER");
        gameOverLabel.getStyleClass().add("gameOverStyle");
        
        newGameButton = new Button("New Game (N)");
        newGameButton.getStyleClass().add("game-button");
        
        VBox content = new VBox(15);
        content.setAlignment(Pos.CENTER);
        content.getChildren().addAll(gameOverLabel, newGameButton);
        
        setCenter(content);
        
        // Set a semi-transparent dark background for the entire panel
        setStyle("-fx-background-color: rgba(0, 0, 0, 0.85); -fx-background-radius: 10px; -fx-padding: 20px;");
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
