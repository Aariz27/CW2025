package com.comp2042.ui;

import com.comp2042.game.data.ViewData;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;

/**
 * Handles data-to-UI transformations for game rendering.
 * Extracted from GuiController following Single Responsibility Principle -
 * separates rendering logic from input handling and timing.
 */
public final class GameViewModel {

    // Keep brick sizing consistent with GuiController (24px)
    private static final int BRICK_SIZE = 24;

    /** Creates a view model for positioning and rendering bricks and board. */
    public GameViewModel() { }

    /**
     * Positions the brick panel based on the current brick's position.
     * 
     * @param gamePanel the main game panel
     * @param brickPanel the brick panel to position
     * @param brick the view data containing brick position
     */
    public void positionBrickPanel(GridPane gamePanel, GridPane brickPanel, ViewData brick) {
        brickPanel.setLayoutX(gamePanel.getLayoutX() + brick.getxPosition() * brickPanel.getVgap() + brick.getxPosition() * BRICK_SIZE);
        brickPanel.setLayoutY(-42 + gamePanel.getLayoutY() + brick.getyPosition() * brickPanel.getHgap() + brick.getyPosition() * BRICK_SIZE);
    }

    /**
     * Updates the brick rectangles with colors from the view data.
     * 
     * @param rectangles the rectangle array to update
     * @param brick the view data containing brick shape and colors
     */
    public void updateBrickRectangles(Rectangle[][] rectangles, ViewData brick) {
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                setRectangleData(brick.getBrickData()[i][j], rectangles[i][j]);
            }
        }
    }

    /**
     * Updates the board display matrix with the current board state.
     * 
     * @param displayMatrix the rectangle array representing the board display
     * @param board the board state matrix
     */
    public void updateBoard(Rectangle[][] displayMatrix, int[][] board) {
        for (int i = 2; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                setRectangleData(board[i][j], displayMatrix[i][j]);
            }
        }
    }

    private void setRectangleData(int color, Rectangle rectangle) {
        rectangle.setFill(ColorMapper.getFillColor(color));
        rectangle.setArcHeight(9);
        rectangle.setArcWidth(9);
    }
}
