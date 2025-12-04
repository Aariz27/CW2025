package com.comp2042.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 * Controls panel displaying all game controls and their functions.
 */
public class ControlsPanel extends BorderPane {

    private final Button backButton;

    /** Builds the controls/instructions screen. */
    public ControlsPanel() {
        // Set background style
        getStyleClass().add("controls-background");

        final Label titleLabel = new Label("CONTROLS");
        titleLabel.getStyleClass().add("bonusStyle");

        final Label movementLabel = new Label("Movement:");
        movementLabel.getStyleClass().add("gameOverStyle");

        final Label movementControls = new Label("← → A D    Move Left/Right\n↓ S        Soft Drop (1 point)\n↑ W        Rotate Piece");
        movementControls.getStyleClass().add("controlsText");
        movementControls.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        final Label actionsLabel = new Label("Actions:");
        actionsLabel.getStyleClass().add("gameOverStyle");

        final Label actionControls = new Label(
            "SPACE      Hard Drop (2 points/cell)\n" +
            "C          Hold/Swap Piece\n" +
            "G          Slow Time (every 3 rows, 5s)\n" +
            "U          Undo Last Move (every 5 rows)\n" +
            "P          Pause/Resume\n" +
            "T          Change Theme"
        );
        actionControls.getStyleClass().add("controlsText");
        actionControls.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        final Label gameLabel = new Label("Game:");
        gameLabel.getStyleClass().add("gameOverStyle");

        final Label gameControls = new Label("N          New Game\nESC        Quit");
        gameControls.getStyleClass().add("controlsText");
        gameControls.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        backButton = new Button("Back to Menu");
        backButton.getStyleClass().add("game-button");

        VBox content = new VBox(20);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(20));
        content.getChildren().addAll(
            titleLabel,
            movementLabel,
            movementControls,
            actionsLabel,
            actionControls,
            gameLabel,
            gameControls,
            backButton
        );

        setCenter(content);
    }

    /**
     * Gets the back button for event binding.
     *
     * @return the back button
     */
    public Button getBackButton() {
        return backButton;
    }

}
