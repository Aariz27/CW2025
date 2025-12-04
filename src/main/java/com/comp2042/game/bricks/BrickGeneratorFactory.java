package com.comp2042.game.bricks;

/**
 * Factory for creating brick generator instances.
 * Decouples brick generation from board implementation - new generator types
 * can be added without modifying existing code (Open/Closed Principle).
 */
public class BrickGeneratorFactory {

    /** Prevent instantiation of the factory utility class. */
    private BrickGeneratorFactory() { }

    /**
     * Supported generator types.
     */
    public enum GeneratorType {
        /** Random uniform generator. */
        RANDOM
        // Can be extended: WEIGHTED, SEQUENTIAL, etc.
    }

    /**
     * Creates a brick generator based on type.
     * 
     * @param type the generator type
     * @return a BrickGenerator instance
     */
    public static BrickGenerator create(GeneratorType type) {
        return switch (type) {
            case RANDOM -> new RandomBrickGenerator();
        };
    }

    /**
     * Creates the default brick generator (random).
     * 
     * @return a default BrickGenerator instance
     */
    public static BrickGenerator createDefault() {
        return create(GeneratorType.RANDOM);
    }
}
