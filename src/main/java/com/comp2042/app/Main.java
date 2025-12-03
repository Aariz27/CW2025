package com.comp2042.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

import com.comp2042.ui.GuiController;
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

    /**
     * Initializes and starts the Tetris game application.
     * Loads the FXML layout, creates the game board, and wires up
     * the controller with dependency injection.
     * 
     * @param primaryStage the primary stage for this application
     * @throws Exception if FXML loading fails or resources are missing
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        URL location = getClass().getClassLoader().getResource("gameLayout.fxml");
        ResourceBundle resources = null;
        FXMLLoader fxmlLoader = new FXMLLoader(location, resources);
        Parent root = fxmlLoader.load();
        GuiController c = fxmlLoader.getController();

        primaryStage.setTitle("TetrisJFX");
        Scene scene = new Scene(root, 432, 612);
        primaryStage.setScene(scene);
        primaryStage.show();

        Board board = new SimpleBoard(25, 10);
        new GameController(c, board);
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