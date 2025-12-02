package com.comp2042.ui;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.effect.Reflection;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ResourceBundle;

import com.comp2042.game.events.InputEventListener;
import com.comp2042.game.events.MoveEvent;
import javafx.scene.control.Label;
import com.comp2042.game.events.EventType;
import com.comp2042.game.events.EventSource;
import com.comp2042.game.data.ViewData;
import com.comp2042.game.data.DownData;
import com.comp2042.game.level.LevelManager;
import com.comp2042.game.score.HighScoreManager;
import javafx.beans.binding.Bindings;

/**
 * Main GUI controller for the Tetris game interface.
 * Manages JavaFX UI components, handles rendering of the game board,
 * active brick, ghost piece, and coordinates user input with game logic.
 * 
 * <p>This controller implements multiple design patterns:
 * <ul>
 *   <li><b>MVC Pattern:</b> Acts as the View, delegating game logic to GameController</li>
 *   <li><b>Observer Pattern:</b> Uses JavaFX property bindings for automatic UI updates</li>
 *   <li><b>Dependency Injection:</b> Receives InputEventListener through setter injection</li>
 * </ul>
 * 
 * <p>Key responsibilities:
 * <ul>
 *   <li>Rendering the game board, active brick, and ghost piece</li>
 *   <li>Handling keyboard input and delegating to game controller</li>
 *   <li>Managing game timer and pause/resume functionality</li>
 *   <li>Displaying score, level, and notification animations</li>
 * </ul>
 */
public class GuiController implements Initializable {

    /** Size of each brick block in pixels */
    private static final int BRICK_SIZE = 20;
    

    /** Main game board grid panel */
    @FXML
    private GridPane gamePanel;

    /** Container for notification panels (score bonuses, level ups) */
    @FXML
    private Group groupNotification;

    /** Panel for displaying the active brick */
    @FXML
    private GridPane brickPanel;

    /** Panel for displaying the ghost piece (landing preview) */
    @FXML
    private GridPane ghostPanel;

    /** Panel displayed when game is over */
    @FXML
    private GameOverPanel gameOverPanel;

    /** Label displaying the current score */
    @FXML
    private Label scoreLabel;
    
    /** Label displaying the current level */
    @FXML
    private Label levelLabel;
    
    /** Label displaying the high score */
    @FXML
    private Label highScoreLabel;

    /** 2D array of rectangles representing the game board cells */
    private Rectangle[][] displayMatrix;
    
    /** Manages level progression and configuration */
    private LevelManager levelManager;
    
    /** Manages high score tracking and persistence */
    private HighScoreManager highScoreManager;
    
    /** Panel displayed when game is paused */
    private PausePanel pausePanel;

    /** Listener for handling input events from the UI */
    private InputEventListener eventListener;

    /** 2D array of rectangles representing the active brick */
    private Rectangle[][] rectangles;
    
    /** 2D array of rectangles representing the ghost piece */
    private Rectangle[][] ghostRectangles;
    
    /** Timer for automatic brick dropping */
    private GameTimer gameTimer;

    /** Property tracking whether the game is paused */
    private final BooleanProperty isPause = new SimpleBooleanProperty();

    /** Property tracking whether the game is over */
    private final BooleanProperty isGameOver = new SimpleBooleanProperty();

    /** Handler for keyboard input */
    private InputHandler inputHandler;

    /** View model for game rendering operations */
    private final GameViewModel gameViewModel = new GameViewModel();

    /**
     * Initializes the GUI controller when the FXML layout is loaded.
     * Sets up fonts, focus handling, game timer, and visual effects.
     * 
     * @param location the location used to resolve relative paths (unused)
     * @param resources the resources used for localization (unused)
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Font.loadFont(getClass().getClassLoader().getResource("digital.ttf").toExternalForm(), 38);
        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();

        gameTimer = new GameTimer();

        gameOverPanel.setVisible(false);
        
        // Bind new game button in game over panel
        gameOverPanel.getNewGameButton().setOnAction(e -> newGame(e));

        final Reflection reflection = new Reflection();
        reflection.setFraction(0.8);
        reflection.setTopOpacity(0.9);
        reflection.setTopOffset(-12);
    }

    /**
     * Initializes the game view with the board matrix and initial brick.
     * Creates all rectangle arrays for the board, active brick, and ghost piece.
     * Starts the game timer after initialization.
     * 
     * @param boardMatrix the 2D array representing the game board state
     * @param brick the initial brick's view data
     */
    public void initGameView(int[][] boardMatrix, ViewData brick) {
        displayMatrix = new Rectangle[boardMatrix.length][boardMatrix[0].length];
        for (int i = 2; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(Color.TRANSPARENT);
                displayMatrix[i][j] = rectangle;
                gamePanel.add(rectangle, j, i - 2);
            }
        }

        // Initialize active brick panel
        rectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(ColorMapper.getFillColor(brick.getBrickData()[i][j]));
                rectangles[i][j] = rectangle;
                brickPanel.add(rectangle, j, i);
            }
        }
        gameViewModel.positionBrickPanel(gamePanel, brickPanel, brick);

        // Initialize ghost panel with semi-transparent rectangles
        ghostRectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(ColorMapper.getGhostFillColor(brick.getBrickData()[i][j]));
                rectangle.setArcHeight(9);
                rectangle.setArcWidth(9);
                ghostRectangles[i][j] = rectangle;
                ghostPanel.add(rectangle, j, i);
            }
        }
        // Position ghost panel at the landing position
        positionGhostPanel(brick);

        // Start timer with level 1 speed (400ms)
        startTimerWithCurrentLevelSpeed();
    }
    
    /**
     * Starts the game timer with the current level's drop speed.
     */
    private void startTimerWithCurrentLevelSpeed() {
        if (levelManager != null) {
            double speed = levelManager.getCurrentLevelConfig().getDropSpeedMs();
            gameTimer.start(() -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD)), speed);
        } else {
            // Fallback to default speed if level manager not set
            gameTimer.start(() -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD)), 400.0);
        }
    }



    /**
     * Refreshes the active brick and ghost piece display with new view data.
     * Only updates if the game is not paused.
     * 
     * @param brick the updated view data containing brick position and shape
     */
    private void refreshBrick(ViewData brick) {
        if (isPause.getValue() == Boolean.FALSE) {
            gameViewModel.positionBrickPanel(gamePanel, brickPanel, brick);
            gameViewModel.updateBrickRectangles(rectangles, brick);
            
            // Update ghost piece position and appearance
            positionGhostPanel(brick);
            updateGhostRectangles(brick);
        }
    }
    
    /**
     * Positions the ghost panel at the landing position from ViewData.
     * 
     * @param brick the view data containing ghost position
     */
    private void positionGhostPanel(ViewData brick) {
        ghostPanel.setLayoutX(gamePanel.getLayoutX() + brick.getGhostX() * ghostPanel.getVgap() + brick.getGhostX() * BRICK_SIZE);
        ghostPanel.setLayoutY(-42 + gamePanel.getLayoutY() + brick.getGhostY() * ghostPanel.getHgap() + brick.getGhostY() * BRICK_SIZE);
    }
    
    /**
     * Updates the ghost rectangles with semi-transparent colors.
     * 
     * @param brick the view data containing brick shape and colors
     */
    private void updateGhostRectangles(ViewData brick) {
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                ghostRectangles[i][j].setFill(ColorMapper.getGhostFillColor(brick.getBrickData()[i][j]));
                ghostRectangles[i][j].setArcHeight(9);
                ghostRectangles[i][j].setArcWidth(9);
            }
        }
    }

    /**
     * Refreshes the game board background with the current board state.
     * 
     * @param board the 2D array representing the current board state
     */
    public void refreshGameBackground(int[][] board) {
        gameViewModel.updateBoard(displayMatrix, board);
    }

    

    /**
     * Handles downward movement of the active brick.
     * Shows score notification if rows are cleared.
     * 
     * @param event the movement event containing source information
     */
    private void moveDown(MoveEvent event) {
        if (isPause.getValue() == Boolean.FALSE) {
            DownData downData = eventListener.onDownEvent(event);
            if (downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0) {
                // Calculate final score with level multiplier for display
                int baseScore = downData.getClearRow().getScoreBonus();
                int finalScore = baseScore;
                if (levelManager != null) {
                    double multiplier = levelManager.getCurrentLevelConfig().getScoreMultiplier();
                    finalScore = (int) (baseScore * multiplier);
                }
                NotificationPanel notificationPanel = new NotificationPanel("+" + finalScore);
                groupNotification.getChildren().add(notificationPanel);
                notificationPanel.showScore(groupNotification.getChildren());
            }
            refreshBrick(downData.getViewData());
        }
        gamePanel.requestFocus();
    }
    
    /**
     * Handles hard drop - instantly drops piece to ghost position.
     * Called when player presses SPACE bar.
     */
    private void handleHardDrop() {
        if (isPause.getValue() == Boolean.FALSE) {
            DownData downData = eventListener.onHardDropEvent(new MoveEvent(EventType.HARD_DROP, EventSource.USER));
            if (downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0) {
                // Calculate final score with level multiplier for display
                int baseScore = downData.getClearRow().getScoreBonus();
                int finalScore = baseScore;
                if (levelManager != null) {
                    double multiplier = levelManager.getCurrentLevelConfig().getScoreMultiplier();
                    finalScore = (int) (baseScore * multiplier);
                }
                NotificationPanel notificationPanel = new NotificationPanel("+" + finalScore);
                groupNotification.getChildren().add(notificationPanel);
                notificationPanel.showScore(groupNotification.getChildren());
            }
            refreshBrick(downData.getViewData());
        }
        gamePanel.requestFocus();
    }

    /**
     * Sets the input event listener and configures keyboard input handling.
     * Implements dependency injection pattern.
     * 
     * @param eventListener the listener to handle game input events
     */
    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;

        // Build the InputHandler now that we have an eventListener
        inputHandler = new InputHandler(
                this.eventListener,
                isPause,
                isGameOver,
                () -> newGame(null), // N key
                () -> moveDown(new MoveEvent(EventType.DOWN, EventSource.USER)), // Down/S key - soft drop
                this::handleHardDrop, // Space key - hard drop
                this::togglePause, // P key - pause/resume
                this::refreshBrick // Refresh brick immediately after moves to fix latency
        );
    
        gamePanel.setOnKeyPressed(inputHandler.build());
    }

    /**
     * Binds the score property to the score label using JavaFX property binding.
     * Implements the Observer pattern for automatic UI updates.
     * 
     * @param integerProperty the score property to bind
     */
    public void bindScore(IntegerProperty integerProperty) {
        if (scoreLabel != null) {
            scoreLabel.textProperty().bind(
                Bindings.createStringBinding(
                    () -> "Score: " + integerProperty.getValue(),
                    integerProperty
                )
            );
        }
    }
    
    /**
     * Binds the level property to the level label.
     * 
     * @param levelManager the level manager containing the level property
     */
    public void bindLevel(LevelManager levelManager) {
        this.levelManager = levelManager;
        if (levelLabel != null && levelManager != null) {
            levelLabel.textProperty().bind(
                Bindings.createStringBinding(
                    () -> "Level: " + levelManager.levelProperty().getValue(),
                    levelManager.levelProperty()
                )
            );
        }
    }
    
    /**
     * Binds the high score property to the high score label.
     * 
     * @param highScoreManager the high score manager containing the high score property
     */
    public void bindHighScore(HighScoreManager highScoreManager) {
        this.highScoreManager = highScoreManager;
        if (highScoreLabel != null && highScoreManager != null) {
            highScoreLabel.textProperty().bind(
                Bindings.createStringBinding(
                    () -> "High Score: " + highScoreManager.highScoreProperty().getValue(),
                    highScoreManager.highScoreProperty()
                )
            );
        }
    }
    
    /**
     * Called when the player levels up.
     * Shows a notification and updates the timer speed.
     * 
     * @param newLevel the new level number
     */
    public void onLevelUp(int newLevel) {
        // Show level-up notification
        NotificationPanel notificationPanel = new NotificationPanel("LEVEL " + newLevel + "!");
        groupNotification.getChildren().add(notificationPanel);
        notificationPanel.showScore(groupNotification.getChildren());
        
        // Update timer speed for new level
        startTimerWithCurrentLevelSpeed();
    }

    /**
     * Handles game over state by stopping the timer, showing the game over panel,
     * and checking for new high scores.
     */
    public void gameOver() {
        gameTimer.stop();
        
        // Clear pause panel if it exists (but not the game over panel container)
        if (pausePanel != null) {
            groupNotification.getChildren().remove(pausePanel);
            pausePanel = null;
        }
        
        // Check for new high score
        if (highScoreManager != null && highScoreManager.isNewHighScore()) {
            NotificationPanel notificationPanel = new NotificationPanel("NEW HIGH SCORE!");
            groupNotification.getChildren().add(notificationPanel);
            notificationPanel.showScore(groupNotification.getChildren());
            highScoreManager.onGameEnd(); // Save high score
        }
        
        gameOverPanel.setVisible(true);
        isGameOver.setValue(Boolean.TRUE);
        isPause.setValue(Boolean.FALSE); // Ensure not in pause state
    }

    /**
     * Starts a new game by resetting all state and restarting the timer.
     * 
     * @param actionEvent the action event (may be null if called programmatically)
     */
    public void newGame(ActionEvent actionEvent) {
        gameTimer.stop();
        gameOverPanel.setVisible(false);
        
        // Clear pause panel if it exists
        if (pausePanel != null) {
            hidePauseNotification();
        }
        
        eventListener.createNewGame();
        gamePanel.requestFocus();
        startTimerWithCurrentLevelSpeed();
        isPause.setValue(Boolean.FALSE);
        isGameOver.setValue(Boolean.FALSE);
    }

    /**
     * Handles pause game action by restoring focus to the game panel.
     * 
     * @param actionEvent the action event
     */
    public void pauseGame(ActionEvent actionEvent) {
        gamePanel.requestFocus();
    }
    
    /**
     * Toggles the pause state of the game.
     * Pauses or resumes the game timer and shows/hides the pause notification.
     */
    public void togglePause() {
        if (isGameOver.getValue() == Boolean.TRUE) {
            return; // Don't allow pause when game is over
        }
        
        boolean currentPauseState = isPause.getValue();
        isPause.setValue(!currentPauseState);
        
        if (isPause.getValue()) {
            // Pausing the game
            gameTimer.stop();
            showPauseNotification();
        } else {
            // Resuming the game
            startTimerWithCurrentLevelSpeed();
            hidePauseNotification();
        }
    }
    
    /**
     * Shows a pause panel with resume and new game options.
     */
    private void showPauseNotification() {
        pausePanel = new PausePanel();
        // Bind pause panel buttons
        pausePanel.getResumeButton().setOnAction(e -> {
            togglePause();
            gamePanel.requestFocus();
        });
        pausePanel.getNewGameButton().setOnAction(e -> newGame(e));
        
        groupNotification.getChildren().add(pausePanel);
    }
    
    /**
     * Hides the pause panel by clearing all notifications.
     */
    private void hidePauseNotification() {
        groupNotification.getChildren().clear();
        pausePanel = null;
    }
    
    /**
     * Initializes a grid of rectangles for displaying brick shapes.
     * Reduces code duplication for creating brick and ghost panels.
     * 
     * @param brickData the brick shape data matrix
     * @param targetPane the GridPane to add rectangles to
     * @param isGhost whether this is for a ghost piece (semi-transparent with rounded corners)
     * @return a 2D array of Rectangle objects
     */
    private Rectangle[][] initRectangleGrid(int[][] brickData, GridPane targetPane, boolean isGhost) {
        Rectangle[][] grid = new Rectangle[brickData.length][brickData[0].length];
        for (int i = 0; i < brickData.length; i++) {
            for (int j = 0; j < brickData[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                if (isGhost) {
                    rectangle.setFill(ColorMapper.getGhostFillColor(brickData[i][j]));
                    rectangle.setArcHeight(9);
                    rectangle.setArcWidth(9);
                } else {
                    rectangle.setFill(ColorMapper.getFillColor(brickData[i][j]));
                }
                grid[i][j] = rectangle;
                targetPane.add(rectangle, j, i);
            }
        }
        return grid;
    }
}
