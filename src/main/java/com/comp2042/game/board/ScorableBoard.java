package com.comp2042.game.board;

import com.comp2042.game.score.Score;

/**
 * Interface for score access.
 * Segregated interface following Interface Segregation Principle.
 */
public interface ScorableBoard {
    /**
     * Gets the score object for this board.
     * @return the Score object containing current score and score property
     */
    Score getScore();
}

