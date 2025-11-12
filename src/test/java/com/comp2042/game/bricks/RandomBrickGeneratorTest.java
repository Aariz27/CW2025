package com.comp2042.game.bricks;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RandomBrickGeneratorTest {

    @Test
    void getBrickReturnsNextBrickFromQueue() {
        RandomBrickGenerator generator = new RandomBrickGenerator();

        Brick peeked = generator.getNextBrick();
        Brick drawn = generator.getBrick();

        assertSame(peeked, drawn, "getBrick should return the same instance that was previewed");
        assertNotNull(generator.getNextBrick(), "Preview queue should be refilled after drawing");
    }
}

