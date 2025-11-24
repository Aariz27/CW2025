package com.comp2042.game.bricks;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotSame;

class BrickGeneratorFactoryTest {

    @Test
    void createRandomGeneratorReturnsRandomBrickGenerator() {
        BrickGenerator generator =
                BrickGeneratorFactory.create(BrickGeneratorFactory.GeneratorType.RANDOM);

        assertInstanceOf(RandomBrickGenerator.class, generator);
    }

    @Test
    void createDefaultDelegatesToRandom() {
        BrickGenerator first = BrickGeneratorFactory.createDefault();
        BrickGenerator second = BrickGeneratorFactory.createDefault();

        assertInstanceOf(RandomBrickGenerator.class, first);
        assertInstanceOf(RandomBrickGenerator.class, second);
        assertNotSame(first, second);
    }
}

