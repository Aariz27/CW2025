package com.comp2042.game.bricks;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Random brick generator that creates bricks randomly from all seven tetromino types.
 * Maintains a queue of upcoming bricks to ensure smooth preview functionality.
 * 
 * <p>This implementation follows the Strategy pattern and uses a queue-based
 * approach to pre-generate bricks for preview purposes.
 */
public class RandomBrickGenerator implements BrickGenerator {

    private final List<Brick> brickList;

    private final Deque<Brick> nextBricks = new ArrayDeque<>();

    /**
     * Constructs a random brick generator and initializes it with
     * all seven tetromino types. Pre-generates two bricks for the queue.
     */
    public RandomBrickGenerator() {
        brickList = new ArrayList<>();
        brickList.add(new IBrick());
        brickList.add(new JBrick());
        brickList.add(new LBrick());
        brickList.add(new OBrick());
        brickList.add(new SBrick());
        brickList.add(new TBrick());
        brickList.add(new ZBrick());
        nextBricks.add(brickList.get(ThreadLocalRandom.current().nextInt(brickList.size())));
        nextBricks.add(brickList.get(ThreadLocalRandom.current().nextInt(brickList.size())));
    }

    /**
     * Returns the next brick and generates a new one for the queue.
     * Ensures at least one brick is always available for preview.
     * 
     * @return the next brick from the queue
     */
    @Override
    public Brick getBrick() {
        if (nextBricks.size() <= 1) {
            nextBricks.add(brickList.get(ThreadLocalRandom.current().nextInt(brickList.size())));
        }
        return nextBricks.poll();
    }

    /**
     * Returns a preview of the next brick without removing it from the queue.
     * 
     * @return the next brick (peek)
     */
    @Override
    public Brick getNextBrick() {
        return nextBricks.peek();
    }

    /**
     * Creates a defensive copy of the current upcoming brick queue.
     *
     * @return a copy of the queue
     */
    public Deque<Brick> copyQueue() {
        return new ArrayDeque<>(nextBricks);
    }

    /**
     * Restores the upcoming brick queue from the provided snapshot.
     *
     * @param snapshot the queue snapshot to restore
     */
    public void restoreQueue(Deque<Brick> snapshot) {
        nextBricks.clear();
        nextBricks.addAll(snapshot);
    }
}
