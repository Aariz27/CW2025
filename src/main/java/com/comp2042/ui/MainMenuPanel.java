package com.comp2042.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 * Main menu panel displayed when the application starts.
 * Shows game title and provides options to start game or view controls.
 */
public class MainMenuPanel extends BorderPane {

    private final Button startGameButton;
    private final Button controlsButton;

    public MainMenuPanel() {
        // Set background style
        getStyleClass().add("main-menu-background");

        final Label titleLabel = new Label("TETRIS");
        titleLabel.getStyleClass().add("bonusStyle");

        final Label subtitleLabel = new Label("JavaFX Edition");
        subtitleLabel.getStyleClass().add("gameOverStyle");

        startGameButton = new Button("Start Game");
        startGameButton.getStyleClass().add("game-button");

        controlsButton = new Button("Controls");
        controlsButton.getStyleClass().add("game-button");

        VBox content = new VBox(20);
        content.setAlignment(Pos.CENTER);
        content.getChildren().addAll(titleLabel, subtitleLabel, startGameButton, controlsButton);

        setCenter(content);
    }

    /**
     * Gets the start game button for event binding.
     *
     * @return the start game button
     */
    public Button getStartGameButton() {
        return startGameButton;
    }

    /**
     * Gets the controls button for event binding.
     *
     * @return the controls button
     */
    public Button getControlsButton() {
        return controlsButton;
    }

}
