package com.comp2042.ui;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.effect.Reflection;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.collections.ObservableList;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

import com.comp2042.game.events.InputEventListener;
import com.comp2042.game.events.MoveEvent;
import javafx.scene.control.Label;
import com.comp2042.game.events.EventType;
import com.comp2042.game.events.EventSource;
import com.comp2042.game.data.ViewData;
import com.comp2042.game.data.DownData;
import com.comp2042.game.data.UndoData;
import com.comp2042.game.controller.GameController;
import com.comp2042.game.level.LevelManager;
import com.comp2042.game.score.HighScoreManager;
import com.comp2042.ui.theme.Theme;
import com.comp2042.ui.theme.ThemeManager;
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
    private static final int BRICK_SIZE = 24;
    /** Additional milliseconds to apply when slowing time */
    private static final double TIME_SLOW_OFFSET_MS = 500.0;
    /** Duration in milliseconds for the slow-time effect */
    private static final int TIME_SLOW_DURATION_MS = 5000;
    

    /** Main game board grid panel */
    @FXML
    private GridPane gamePanel;

    /** Container border for the main game board */
    @FXML
    private BorderPane gameBoard;

    /** Container for notification panels (score bonuses, level ups) */
    @FXML
    private Group groupNotification;

    /** Panel for displaying the active brick */
    @FXML
    private GridPane brickPanel;

    /** Panel for displaying the ghost piece (landing preview) */
    @FXML
    private GridPane ghostPanel;
    
    /** Panel for displaying the next piece preview */
    @FXML
    private GridPane nextBrickPanel;

    /** Container pane for the next brick preview */
    @FXML
    private Pane nextBrickContainer;

    /** Panel for displaying the hold piece */
    @FXML
    private GridPane holdBrickPanel;

    /** Container pane for the hold brick display */
    @FXML
    private Pane holdBrickContainer;

    /** Panel displayed when game is over */
    @FXML
    private GameOverPanel gameOverPanel;
    
    /** Gray overlay shown when game is over */
    @FXML
    private Rectangle gameOverOverlay;

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
    /** Direct reference for ability-related actions */
    private GameController gameController;

    /** 2D array of rectangles representing the active brick */
    private Rectangle[][] rectangles;
    
    /** 2D array of rectangles representing the ghost piece */
    private Rectangle[][] ghostRectangles;
    
    /** 2D array of rectangles representing the next piece preview */
    private Rectangle[][] nextBrickRectangles;

    /** 2D array of rectangles representing the hold piece */
    private Rectangle[][] holdBrickRectangles;
    
    /** Timer for automatic brick dropping */
    private GameTimer gameTimer;
    /** Active slow-time flag */
    private boolean timeSlowActive;
    /** Timer to end slow-time effect */
    private PauseTransition timeSlowTimer;

    /** Property tracking whether the game is paused */
    private final BooleanProperty isPause = new SimpleBooleanProperty();

    /** Property tracking whether the game is over */
    private final BooleanProperty isGameOver = new SimpleBooleanProperty();

    /** Handler for keyboard input */
    private InputHandler inputHandler;

    /** View model for game rendering operations */
    private final GameViewModel gameViewModel = new GameViewModel();

    /** Cache of current board state for repainting during theme switches */
    private int[][] currentBoardMatrix;
    
    /** Cache of current view data for repainting during theme switches */
    private ViewData currentViewData;

    /**
     * Initializes the GUI controller when the FXML layout is loaded.
     * Sets up fonts, focus handling, game timer, visual effects, and theme handling.
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
        gameOverOverlay.setVisible(false);
        
        // Bind new game button in game over panel
        gameOverPanel.getNewGameButton().setOnAction(e -> newGame(e));

        final Reflection reflection = new Reflection();
        reflection.setFraction(0.8);
        reflection.setTopOpacity(0.9);
        reflection.setTopOffset(-12);
        
        initThemeHandling();
    }

    /**
     * Sets up the theme handling logic.
     * Listens for theme changes to update stylesheets and repaint the game.
     */
    private void initThemeHandling() {
        ThemeManager.getInstance().currentThemeProperty().addListener((obs, oldTheme, newTheme) -> {
            if (newTheme != null && gamePanel.getScene() != null) {
                // Update stylesheet
                gamePanel.getScene().getStylesheets().clear();
                String cssPath = getClass().getClassLoader().getResource(newTheme.getStylesheet()).toExternalForm();
                gamePanel.getScene().getStylesheets().add(cssPath);
                
                // Update background color if needed (though mostly handled by CSS)
                if (gameBoard != null) {
                    // Force a layout pass or style update if needed
                }
                
                // Repaint board and active piece with new theme colors
                if (currentBoardMatrix != null) {
                    refreshGameBackground(currentBoardMatrix);
                }
                if (currentViewData != null) {
                    // Refresh brick also refreshes ghost pieces and next brick preview
                    refreshBrick(currentViewData);
                }
                
                // Show notification
                NotificationPanel notificationPanel = new NotificationPanel("THEME: " + newTheme.getName().toUpperCase());
                groupNotification.getChildren().add(notificationPanel);
                notificationPanel.showScore(groupNotification.getChildren());
            }
        });
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
        this.currentBoardMatrix = boardMatrix;
        this.currentViewData = brick;
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

        // Initialize next brick preview panel
        initNextBrickPreview(brick);

        // Start timer with level 1 speed (400ms)
        startTimerWithCurrentLevelSpeed();
    }
    
    /**
     * Starts the game timer with the current level's drop speed.
     */
    private void startTimerWithCurrentLevelSpeed() {
        if (levelManager != null) {
            double speed = levelManager.getCurrentLevelConfig().getDropSpeedMs();
            if (timeSlowActive) {
                speed += TIME_SLOW_OFFSET_MS;
            }
            gameTimer.start(() -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD)), speed);
        } else {
            // Fallback to default speed if level manager not set
            double speed = timeSlowActive ? 400.0 + TIME_SLOW_OFFSET_MS : 400.0;
            gameTimer.start(() -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD)), speed);
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
            this.currentViewData = brick;
            gameViewModel.positionBrickPanel(gamePanel, brickPanel, brick);
            gameViewModel.updateBrickRectangles(rectangles, brick);
            
            // Update ghost piece position and appearance
            positionGhostPanel(brick);
            updateGhostRectangles(brick);

            // Update next brick preview
            updateNextBrickPreview(brick);

            // Update hold brick preview
            updateHoldBrickPreview(brick);
        }
    }
    
    /**
     * Positions the ghost panel at the landing position from ViewData.
     * 
     * @param brick the view data containing ghost position
     */
    private void positionGhostPanel(ViewData brick) {
        ghostPanel.setLayoutX(gamePanel.getLayoutX() + brick.getGhostX() * ghostPanel.getVgap() + brick.getGhostX() * BRICK_SIZE);
        ghostPanel.setLayoutY(-50 + gamePanel.getLayoutY() + brick.getGhostY() * ghostPanel.getHgap() + brick.getGhostY() * BRICK_SIZE);
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
     * Initializes the next brick preview panel.
     * Creates rectangles to display the upcoming piece.
     *
     * @param brick the initial view data containing next brick information
     */
    private void initNextBrickPreview(ViewData brick) {
        if (brick.getNextBrickData() == null || brick.getNextBrickData().length == 0) {
            return;
        }

        int[][] nextBrickData = brick.getNextBrickData();
        nextBrickRectangles = new Rectangle[nextBrickData.length][nextBrickData[0].length];

        for (int i = 0; i < nextBrickData.length; i++) {
            for (int j = 0; j < nextBrickData[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(ColorMapper.getFillColor(nextBrickData[i][j]));
                rectangle.setArcHeight(9);
                rectangle.setArcWidth(9);
                nextBrickRectangles[i][j] = rectangle;
                nextBrickPanel.add(rectangle, j, i);
            }
        }

        // Initialize hold brick preview as well
        initHoldBrickPreview(brick);
    }

    /**
     * Initializes the hold brick preview panel.
     * Creates rectangles to display the held piece (if any).
     *
     * @param brick the initial view data containing hold brick information
     */
    private void initHoldBrickPreview(ViewData brick) {
        if (brick.getHoldBrickData() == null || brick.getHoldBrickData().length == 0) {
            // No held brick - create empty grid to maintain layout
            holdBrickRectangles = new Rectangle[4][4]; // Standard 4x4 grid
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                    rectangle.setFill(Color.TRANSPARENT);
                    rectangle.setArcHeight(9);
                    rectangle.setArcWidth(9);
                    holdBrickRectangles[i][j] = rectangle;
                    holdBrickPanel.add(rectangle, j, i);
                }
            }
            return;
        }

        int[][] holdBrickData = brick.getHoldBrickData();
        holdBrickRectangles = new Rectangle[holdBrickData.length][holdBrickData[0].length];

        for (int i = 0; i < holdBrickData.length; i++) {
            for (int j = 0; j < holdBrickData[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(ColorMapper.getFillColor(holdBrickData[i][j]));
                rectangle.setArcHeight(9);
                rectangle.setArcWidth(9);
                holdBrickRectangles[i][j] = rectangle;
                holdBrickPanel.add(rectangle, j, i);
            }
        }
    }
    
    /**
     * Updates the next brick preview display.
     * Called whenever a new piece spawns and the next piece changes.
     *
     * @param brick the view data containing updated next brick information
     */
    private void updateNextBrickPreview(ViewData brick) {
        if (brick.getNextBrickData() == null || nextBrickRectangles == null) {
            return;
        }

        int[][] nextBrickData = brick.getNextBrickData();

        // If the next brick shape changed size, reinitialize
        if (nextBrickRectangles.length != nextBrickData.length ||
            nextBrickRectangles[0].length != nextBrickData[0].length) {
            nextBrickPanel.getChildren().clear();
            initNextBrickPreview(brick);
            return;
        }

        // Update colors
        for (int i = 0; i < nextBrickData.length; i++) {
            for (int j = 0; j < nextBrickData[i].length; j++) {
                nextBrickRectangles[i][j].setFill(ColorMapper.getFillColor(nextBrickData[i][j]));
                nextBrickRectangles[i][j].setArcHeight(9);
                nextBrickRectangles[i][j].setArcWidth(9);
            }
        }
    }

    /**
     * Updates the hold brick preview display.
     * Called whenever the held brick changes (hold operation or new game).
     *
     * @param brick the view data containing updated hold brick information
     */
    private void updateHoldBrickPreview(ViewData brick) {
        if (holdBrickRectangles == null) {
            return;
        }

        int[][] holdBrickData = brick.getHoldBrickData();

        // Clear existing rectangles
        holdBrickPanel.getChildren().clear();

        if (holdBrickData == null || holdBrickData.length == 0) {
            // No held brick - create empty grid
            holdBrickRectangles = new Rectangle[4][4];
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                    rectangle.setFill(Color.TRANSPARENT);
                    rectangle.setArcHeight(9);
                    rectangle.setArcWidth(9);
                    holdBrickRectangles[i][j] = rectangle;
                    holdBrickPanel.add(rectangle, j, i);
                }
            }
        } else {
            // Held brick exists - recreate grid with correct dimensions
            holdBrickRectangles = new Rectangle[holdBrickData.length][holdBrickData[0].length];
            for (int i = 0; i < holdBrickData.length; i++) {
                for (int j = 0; j < holdBrickData[i].length; j++) {
                    Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                    rectangle.setFill(ColorMapper.getFillColor(holdBrickData[i][j]));
                    rectangle.setArcHeight(9);
                    rectangle.setArcWidth(9);
                    holdBrickRectangles[i][j] = rectangle;
                    holdBrickPanel.add(rectangle, j, i);
                }
            }
        }
    }

    /**
     * Refreshes the game board background with the current board state.
     * 
     * @param board the 2D array representing the current board state
     */
    public void refreshGameBackground(int[][] board) {
        this.currentBoardMatrix = board;
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
            int linesCleared = 0;
            if (downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0) {
                linesCleared = downData.getClearRow().getLinesRemoved();
                // Update board matrix as lines were removed and blocks shifted
                refreshGameBackground(downData.getClearRow().getNewMatrix());
                
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
            // Trigger a brief shockwave and brick bounce visual when lines are cleared
            if (linesCleared > 0) {
                triggerShockwaveEffect();
                triggerBrickBounceEffect();
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
            int linesCleared = 0;
            if (downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0) {
                linesCleared = downData.getClearRow().getLinesRemoved();
                // Update board matrix as lines were removed and blocks shifted
                refreshGameBackground(downData.getClearRow().getNewMatrix());
                
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
            // Hard drops also trigger the shockwave and brick bounce effects
            if (linesCleared > 0) {
                triggerShockwaveEffect();
                triggerBrickBounceEffect();
            }
            refreshBrick(downData.getViewData());
        }
        gamePanel.requestFocus();
    }

    /**
     * Handles hold piece event by swapping current piece with held piece.
     * Called when player presses the hold key (C).
     */
    private void handleHold() {
        if (isPause.getValue() == Boolean.FALSE) {
            ViewData viewData = eventListener.onHoldEvent(new MoveEvent(EventType.HOLD, EventSource.USER));
            refreshBrick(viewData);
        }
        gamePanel.requestFocus();
    }

    /**
     * Activates the temporary slow-time power if available.
     * Slows automatic drops for a short duration.
     */
    private void handleTimeSlow() {
        if (isPause.getValue() == Boolean.TRUE || isGameOver.getValue() == Boolean.TRUE) {
            gamePanel.requestFocus();
            return;
        }
        if (gameController == null || timeSlowActive) {
            gamePanel.requestFocus();
            return;
        }
        if (!gameController.consumeTimeSlowCharge()) {
            gamePanel.requestFocus();
            return;
        }

        timeSlowActive = true;
        if (timeSlowTimer != null) {
            timeSlowTimer.stop();
        }
        startTimerWithCurrentLevelSpeed();

        NotificationPanel notificationPanel = new NotificationPanel("SLOW TIME");
        groupNotification.getChildren().add(notificationPanel);
        notificationPanel.showScore(groupNotification.getChildren());

        timeSlowTimer = new PauseTransition(Duration.millis(TIME_SLOW_DURATION_MS));
        timeSlowTimer.setOnFinished(e -> {
            timeSlowActive = false;
            startTimerWithCurrentLevelSpeed();
        });
        timeSlowTimer.play();
        gamePanel.requestFocus();
    }

    /**
     * Attempts to undo the last move when the ability is available.
     */
    private void handleUndo() {
        if (isPause.getValue() == Boolean.TRUE || isGameOver.getValue() == Boolean.TRUE) {
            gamePanel.requestFocus();
            return;
        }
        if (gameController == null) {
            gamePanel.requestFocus();
            return;
        }

        UndoData undoData = gameController.undoLastMove();
        if (undoData.isPerformed()) {
            refreshGameBackground(undoData.getBoardMatrix());
            refreshBrick(undoData.getViewData());

            NotificationPanel notificationPanel = new NotificationPanel("UNDO");
            groupNotification.getChildren().add(notificationPanel);
            notificationPanel.showScore(groupNotification.getChildren());
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
        if (eventListener instanceof GameController controller) {
            this.gameController = controller;
        } else {
            this.gameController = null;
        }

        // Build the InputHandler now that we have an eventListener
        inputHandler = new InputHandler(
                this.eventListener,
                isPause,
                isGameOver,
                () -> newGame(null), // N key
                () -> moveDown(new MoveEvent(EventType.DOWN, EventSource.USER)), // Down/S key - soft drop
                this::handleHardDrop, // Space key - hard drop
                this::togglePause, // P key - pause/resume
                this::handleHold, // C key - hold piece
                () -> ThemeManager.getInstance().cycleTheme(), // T key - cycle theme
                this::handleTimeSlow, // G key - slow time power
                this::handleUndo, // U key - undo last move
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
        if (timeSlowTimer != null) {
            timeSlowTimer.stop();
        }
        timeSlowActive = false;
        
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
        
        // Show gray overlay and game over panel
        gameOverOverlay.setVisible(true);
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
        if (timeSlowTimer != null) {
            timeSlowTimer.stop();
        }
        timeSlowActive = false;
        
        // Hide overlay and game over panel
        gameOverOverlay.setVisible(false);
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
     * Hides the pause panel by removing only the pause panel, not all children.
     * This preserves the game over panel which is defined in FXML.
     */
    private void hidePauseNotification() {
        if (pausePanel != null) {
            groupNotification.getChildren().remove(pausePanel);
            pausePanel = null;
        }
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

    // -------------------------
    // Energy Pulse UI helpers
    // -------------------------

    /**
     * Legacy no-op: energy pulse system removed.
     * Kept only to avoid breaking existing call sites.
     */
    private void handleEnergyTick(int linesCleared) {
        // intentionally empty
    }

    /**
     * Legacy no-op: energy pulse system removed.
     */
    private void boostEnergyOnLevelUp() {
        // intentionally empty
    }

    /**
     * Legacy no-op: energy pulse system removed.
     */
    private void resetEnergy() {
        // intentionally empty
    }

    /**
     * Legacy no-op: energy pulse system removed.
     */
    private void clampEnergy() {
        // intentionally empty
    }

    /**
     * Legacy no-op: energy pulse system removed.
     */
    private void updateEnergyUi() {
        // intentionally empty
    }

    /**
     * Applies a short-lived shockwave CSS effect to the game board whenever
     * one or more lines are cleared. Uses a background thread to remove the
     * style after a brief delay without blocking the JavaFX Application Thread.
     */
    private void triggerShockwaveEffect() {
        if (gameBoard == null) {
            return;
        }

        ObservableList<String> styleClasses = gameBoard.getStyleClass();
        if (!styleClasses.contains("shockwave")) {
            styleClasses.add("shockwave");

            // Remove the shockwave class after a short delay so the effect feels like a pulse
            new Thread(() -> {
                try {
                    Thread.sleep(225);
                } catch (InterruptedException ignored) {
                    // Ignore interruption and attempt to clean up the style class
                }
                javafx.application.Platform.runLater(() ->
                        gameBoard.getStyleClass().remove("shockwave")
                );
            }).start();
        }
    }

    /**
     * Applies a coloured stroke bounce to all brick rectangles on the board for a short time
     * whenever one or more lines are cleared. Each brick's border "bounces" using its own fill colour
     * with a fade-in / fade-out animation.
     */
    private void triggerBrickBounceEffect() {
        if (displayMatrix == null && rectangles == null) {
            return;
        }

        applyBounceToMatrix(displayMatrix);
        applyBounceToMatrix(rectangles);
    }

    /**
     * Applies a stroke bounce animation to all non-transparent rectangles in a matrix.
     * Each rectangle's border pulses outward briefly using its fill color.
     * 
     * @param matrix the 2D array of rectangles to animate
     */
    private void applyBounceToMatrix(Rectangle[][] matrix) {
        if (matrix == null) {
            return;
        }
        for (Rectangle[] row : matrix) {
            for (Rectangle rect : row) {
                if (rect == null) {
                    continue;
                }
                if (rect.getFill() instanceof Color color && color.getOpacity() > 0.0) {
                    // Start with no stroke and animate stroke width to create a subtle outline pulse
                    rect.setStroke(color);
                    rect.setStrokeWidth(0.0);

                    javafx.animation.Timeline timeline = new javafx.animation.Timeline(
                        new javafx.animation.KeyFrame(javafx.util.Duration.ZERO,
                            new javafx.animation.KeyValue(rect.strokeWidthProperty(), 0.0)
                        ),
                        new javafx.animation.KeyFrame(javafx.util.Duration.millis(140),
                            new javafx.animation.KeyValue(rect.strokeWidthProperty(), 2.0)
                        ),
                        new javafx.animation.KeyFrame(javafx.util.Duration.millis(280),
                            new javafx.animation.KeyValue(rect.strokeWidthProperty(), 0.0)
                        )
                    );
                    timeline.setOnFinished(e -> {
                        rect.setStroke(null);
                        rect.setStrokeWidth(0.0);
                    });
                    timeline.play();
                }
            }
        }
    }
}
