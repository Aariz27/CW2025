package com.comp2042.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

import com.comp2042.ui.GuiController;
import com.comp2042.ui.MainMenuPanel;
import com.comp2042.ui.ControlsPanel;
import com.comp2042.game.board.Board;
import com.comp2042.game.board.SimpleBoard;
import com.comp2042.game.controller.GameController;

/**
 * Main entry point for the Tetris JavaFX application.
 * Initializes the JavaFX application, loads the FXML layout, and
 * sets up the game controller with dependency injection.
 * 
 * <p>This class follows the MVC pattern where:
 * <ul>
 *   <li>Model: {@link Board} and game state classes</li>
 *   <li>View: {@link GuiController} and FXML layout</li>
 *   <li>Controller: {@link GameController} coordinating logic</li>
 * </ul>
 */
public class Main extends Application {

    /** Default JavaFX application constructor. */
    public Main() { }

    private Scene menuScene;
    private Scene controlsScene;
    private Scene gameScene;
    private GuiController guiController;
    private GameController gameController;

    /**
     * Initializes and starts the Tetris game application.
     * Shows main menu first, then allows navigation to game or controls.
     * 
     * @param primaryStage the primary stage for this application
     * @throws Exception if FXML loading fails or resources are missing
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("TetrisJFX");

        // Create menu scene
        createMenuScene(primaryStage);

        // Create controls scene
        createControlsScene(primaryStage);

        // Create game scene (but don't initialize game yet)
        createGameScene();

        // Show menu first
        primaryStage.setScene(menuScene);
        primaryStage.show();
    }

    /**
     * Creates the main menu scene with Start Game and Controls options.
     */
    private void createMenuScene(Stage primaryStage) {
        MainMenuPanel menuPanel = new MainMenuPanel();

        menuPanel.getStartGameButton().setOnAction(e -> {
            // Initialize game when starting
            if (gameController == null) {
                initializeGame();
            }
            primaryStage.setScene(gameScene);
        });

        menuPanel.getControlsButton().setOnAction(e -> {
            primaryStage.setScene(controlsScene);
        });

        menuScene = new Scene(menuPanel, 432, 612);
        menuScene.getStylesheets().add(getClass().getClassLoader().getResource("window_style.css").toExternalForm());
    }

    /**
     * Creates the controls scene displaying game controls.
     */
    private void createControlsScene(Stage primaryStage) {
        ControlsPanel controlsPanel = new ControlsPanel();

        controlsPanel.getBackButton().setOnAction(e -> {
            primaryStage.setScene(menuScene);
        });

        controlsScene = new Scene(controlsPanel, 432, 612);
        controlsScene.getStylesheets().add(getClass().getClassLoader().getResource("window_style.css").toExternalForm());
    }

    /**
     * Creates the game scene with the actual Tetris game.
     */
    private void createGameScene() throws Exception {
        URL location = getClass().getClassLoader().getResource("gameLayout.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root = fxmlLoader.load();
        guiController = fxmlLoader.getController();

        gameScene = new Scene(root, 432, 612);
        gameScene.getStylesheets().add(getClass().getClassLoader().getResource("window_style.css").toExternalForm());
    }

    /**
     * Initializes the game components when the user chooses to start playing.
     */
    private void initializeGame() {
        Board board = new SimpleBoard(25, 10);
        gameController = new GameController(guiController, board);
    }

    /**
     * Application entry point.
     * 
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        launch(args);
    }
}
