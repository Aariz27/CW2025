package com.comp2042.game.board;

import com.comp2042.game.bricks.Brick;
import com.comp2042.game.bricks.BrickGenerator;
import com.comp2042.game.bricks.BrickGeneratorFactory;
import com.comp2042.game.operations.BrickRotator;
import com.comp2042.game.operations.MatrixOperations;
import com.comp2042.game.data.ViewData;
import com.comp2042.game.data.ClearRow;
import com.comp2042.game.data.NextShapeInfo;
import com.comp2042.game.score.Score;
import com.comp2042.game.level.LinesClearedTracker;
import com.comp2042.game.level.LevelManager;
import com.comp2042.game.level.LevelStrategy;
import com.comp2042.game.level.DefaultLevelStrategy;

import java.awt.Point;

/**
 * Concrete board implementation.
 * Uses Factory pattern for brick generation and implements Board interface
 * to allow different board implementations through polymorphism.
 */
public class SimpleBoard implements Board {

    private final int width;
    private final int height;
    private final BrickGenerator brickGenerator; // Depends on interface, not concrete class
    private final BrickRotator brickRotator;
    private int[][] currentGameMatrix;
    private Point currentOffset;
    private final Score score;
    private final LinesClearedTracker linesTracker;
    private final LevelManager levelManager;

    /**
     * Creates a new SimpleBoard with the specified dimensions.
     * Uses Factory pattern to create brick generator - generator is created
     * through factory rather than directly instantiated, following Dependency Inversion.
     * 
     * @param width the board width
     * @param height the board height
     */
    public SimpleBoard(int width, int height) {
        this.width = width;
        this.height = height;
        currentGameMatrix = new int[width][height];
        brickGenerator = BrickGeneratorFactory.createDefault();
        brickRotator = new BrickRotator();
        score = new Score();
        linesTracker = new LinesClearedTracker();
        LevelStrategy levelStrategy = new DefaultLevelStrategy();
        levelManager = new LevelManager(linesTracker, levelStrategy);
    }

    @Override
    public boolean moveBrickDown() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        p.translate(0, 1);
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }


    @Override
    public boolean moveBrickLeft() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        p.translate(-1, 0);
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }

    @Override
    public boolean moveBrickRight() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        p.translate(1, 0);
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }

    @Override
    public boolean rotateLeftBrick() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        NextShapeInfo nextShape = brickRotator.getNextShape();
        boolean conflict = MatrixOperations.intersect(currentMatrix, nextShape.getShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
        if (conflict) {
            return false;
        } else {
            brickRotator.setCurrentShape(nextShape.getPosition());
            return true;
        }
    }

    @Override
    public boolean trySpawnNewBrick() {
        Brick currentBrick = brickGenerator.getBrick();
        brickRotator.setBrick(currentBrick);
        currentOffset = new Point(4, 0);
        return MatrixOperations.intersect(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
    }

    @Override
    public int[][] getBoardMatrix() {
        return currentGameMatrix;
    }

    @Override
    public ViewData getViewData() {
        int currentX = (int) currentOffset.getX();
        int currentY = (int) currentOffset.getY();
        int ghostY = findDropY(currentX, currentY);
        return new ViewData(
            brickRotator.getCurrentShape(), 
            currentX, 
            currentY, 
            brickGenerator.getNextBrick().getShapeMatrix().get(0),
            currentX,  // ghostX matches current X
            ghostY
        );
    }

    /**
     * Finds the Y position where the piece would land if dropped.
     * Simulates the piece falling down until collision is detected.
     * 
     * @param startX the starting X position
     * @param startY the starting Y position
     * @return the final Y position where the piece lands
     */
    private int findDropY(int startX, int startY) {
        int[][] shape = brickRotator.getCurrentShape();
        int ghostY = startY;
        
        // Loop downward until collision detected
        while (!MatrixOperations.intersect(currentGameMatrix, shape, startX, ghostY + 1)) {
            ghostY++;
        }
        
        return ghostY;
    }

    @Override
    public void mergeBrickToBackground() {
        currentGameMatrix = MatrixOperations.merge(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
    }

    @Override
    public ClearRow clearRows() {
        ClearRow clearRow = MatrixOperations.checkRemoving(currentGameMatrix);
        currentGameMatrix = clearRow.getNewMatrix();
        return clearRow;

    }

    @Override
    public Score getScore() {
        return score;
    }
    
    /**
     * Gets the lines cleared tracker.
     * 
     * @return the LinesClearedTracker
     */
    public LinesClearedTracker getLinesTracker() {
        return linesTracker;
    }
    
    /**
     * Gets the level manager.
     * 
     * @return the LevelManager
     */
    public LevelManager getLevelManager() {
        return levelManager;
    }


    @Override
    public void newGame() {
        currentGameMatrix = new int[width][height];
        score.reset();
        linesTracker.reset();
        levelManager.reset();
        trySpawnNewBrick();
    }
}
