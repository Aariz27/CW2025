package com.comp2042.game.bricks;

// factory to create different types of brick generators
public class BrickGeneratorFactory {

    // types of generators we can create
    public enum GeneratorType {
        RANDOM
        // could add more types later like WEIGHTED, SEQUENTIAL, etc
    }

    // creates a generator based on the type
    public static BrickGenerator create(GeneratorType type) {
        return switch (type) {
            case RANDOM -> new RandomBrickGenerator();
        };
    }

    // default generator (just returns random for now)
    public static BrickGenerator createDefault() {
        return create(GeneratorType.RANDOM);
    }
}

