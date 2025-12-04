package com.comp2042.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 * Panel displayed when the game is paused.
 * Shows "PAUSED" message and provides "Resume" and "New Game" buttons.
 */
public class PausePanel extends BorderPane {

    private final Button resumeButton;
    private final Button newGameButton;

    /** Creates the pause panel with resume and new game actions. */
    public PausePanel() {
        final Label pauseLabel = new Label("PAUSED");
        pauseLabel.getStyleClass().add("bonusStyle");
        
        resumeButton = new Button("Resume (P)");
        resumeButton.getStyleClass().add("game-button");
        
        newGameButton = new Button("New Game (N)");
        newGameButton.getStyleClass().add("game-button");
        
        VBox content = new VBox(10);
        content.setAlignment(Pos.CENTER);
        content.getChildren().addAll(pauseLabel, resumeButton, newGameButton);
        
        setCenter(content);
    }
    
    /**
     * Gets the resume button for event binding.
     * 
     * @return the resume button
     */
    public Button getResumeButton() {
        return resumeButton;
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
