package com.comp2042.game.score;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ScoreTest {

    @Test
    void addIncreasesScoreProperty() {
        Score score = new Score();

        score.add(5);
        score.add(10);

        assertEquals(15, score.scoreProperty().get());
    }

    @Test
    void resetClearsAccumulatedScore() {
        Score score = new Score();
        score.add(20);

        score.reset();

        assertEquals(0, score.scoreProperty().get());
    }
}

