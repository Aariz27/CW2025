package com.comp2042.ui;

import com.comp2042.game.data.ViewData;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;

public final class GameViewModel {

    private static final int BRICK_SIZE = 20;

    public void positionBrickPanel(GridPane gamePanel, GridPane brickPanel, ViewData brick) {
        brickPanel.setLayoutX(gamePanel.getLayoutX() + brick.getxPosition() * brickPanel.getVgap() + brick.getxPosition() * BRICK_SIZE);
        brickPanel.setLayoutY(-42 + gamePanel.getLayoutY() + brick.getyPosition() * brickPanel.getHgap() + brick.getyPosition() * BRICK_SIZE);
    }

    public void updateBrickRectangles(Rectangle[][] rectangles, ViewData brick) {
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                setRectangleData(brick.getBrickData()[i][j], rectangles[i][j]);
            }
        }
    }

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