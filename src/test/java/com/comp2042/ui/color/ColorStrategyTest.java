package com.comp2042.ui.color;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ColorStrategyTest {

    // TransparentColorStrategy tests
    @Test
    void transparentStrategyReturnsTransparent() {
        ColorStrategy strategy = new TransparentColorStrategy();
        assertEquals(Color.TRANSPARENT, strategy.getColor());
    }

    @Test
    void transparentStrategyReturnsCode0() {
        ColorStrategy strategy = new TransparentColorStrategy();
        assertEquals(0, strategy.getColorCode());
    }

    // AquaColorStrategy tests
    @Test
    void aquaStrategyReturnsAqua() {
        ColorStrategy strategy = new AquaColorStrategy();
        assertEquals(Color.AQUA, strategy.getColor());
    }

    @Test
    void aquaStrategyReturnsCode1() {
        ColorStrategy strategy = new AquaColorStrategy();
        assertEquals(1, strategy.getColorCode());
    }

    // BlueVioletColorStrategy tests
    @Test
    void blueVioletStrategyReturnsBlueViolet() {
        ColorStrategy strategy = new BlueVioletColorStrategy();
        assertEquals(Color.BLUEVIOLET, strategy.getColor());
    }

    @Test
    void blueVioletStrategyReturnsCode2() {
        ColorStrategy strategy = new BlueVioletColorStrategy();
        assertEquals(2, strategy.getColorCode());
    }

    // DarkGreenColorStrategy tests
    @Test
    void darkGreenStrategyReturnsDarkGreen() {
        ColorStrategy strategy = new DarkGreenColorStrategy();
        assertEquals(Color.DARKGREEN, strategy.getColor());
    }

    @Test
    void darkGreenStrategyReturnsCode3() {
        ColorStrategy strategy = new DarkGreenColorStrategy();
        assertEquals(3, strategy.getColorCode());
    }

    // YellowColorStrategy tests
    @Test
    void yellowStrategyReturnsYellow() {
        ColorStrategy strategy = new YellowColorStrategy();
        assertEquals(Color.YELLOW, strategy.getColor());
    }

    @Test
    void yellowStrategyReturnsCode4() {
        ColorStrategy strategy = new YellowColorStrategy();
        assertEquals(4, strategy.getColorCode());
    }

    // RedColorStrategy tests
    @Test
    void redStrategyReturnsRed() {
        ColorStrategy strategy = new RedColorStrategy();
        assertEquals(Color.RED, strategy.getColor());
    }

    @Test
    void redStrategyReturnsCode5() {
        ColorStrategy strategy = new RedColorStrategy();
        assertEquals(5, strategy.getColorCode());
    }

    // BeigeColorStrategy tests
    @Test
    void beigeStrategyReturnsBeige() {
        ColorStrategy strategy = new BeigeColorStrategy();
        assertEquals(Color.BEIGE, strategy.getColor());
    }

    @Test
    void beigeStrategyReturnsCode6() {
        ColorStrategy strategy = new BeigeColorStrategy();
        assertEquals(6, strategy.getColorCode());
    }

    // BurlyWoodColorStrategy tests
    @Test
    void burlyWoodStrategyReturnsBurlyWood() {
        ColorStrategy strategy = new BurlyWoodColorStrategy();
        assertEquals(Color.BURLYWOOD, strategy.getColor());
    }

    @Test
    void burlyWoodStrategyReturnsCode7() {
        ColorStrategy strategy = new BurlyWoodColorStrategy();
        assertEquals(7, strategy.getColorCode());
    }

    // WhiteColorStrategy tests
    @Test
    void whiteStrategyReturnsWhite() {
        ColorStrategy strategy = new WhiteColorStrategy();
        assertEquals(Color.WHITE, strategy.getColor());
    }

    @Test
    void whiteStrategyReturnsCodeMinus1() {
        ColorStrategy strategy = new WhiteColorStrategy();
        assertEquals(-1, strategy.getColorCode());
    }

    // General strategy tests
    @Test
    void allStrategiesImplementColorStrategy() {
        ColorStrategy[] strategies = {
            new TransparentColorStrategy(),
            new AquaColorStrategy(),
            new BlueVioletColorStrategy(),
            new DarkGreenColorStrategy(),
            new YellowColorStrategy(),
            new RedColorStrategy(),
            new BeigeColorStrategy(),
            new BurlyWoodColorStrategy(),
            new WhiteColorStrategy()
        };
        
        for (ColorStrategy strategy : strategies) {
            assertNotNull(strategy.getColor());
            assertTrue(strategy.getColor() instanceof Paint);
        }
    }

    @Test
    void allStrategiesHaveUniqueColorCodes() {
        ColorStrategy[] strategies = {
            new TransparentColorStrategy(),
            new AquaColorStrategy(),
            new BlueVioletColorStrategy(),
            new DarkGreenColorStrategy(),
            new YellowColorStrategy(),
            new RedColorStrategy(),
            new BeigeColorStrategy(),
            new BurlyWoodColorStrategy()
        };
        
        // Check for unique codes (except WhiteColorStrategy which is fallback)
        for (int i = 0; i < strategies.length; i++) {
            for (int j = i + 1; j < strategies.length; j++) {
                assertNotEquals(strategies[i].getColorCode(), strategies[j].getColorCode(),
                    strategies[i].getClass().getSimpleName() + " and " + 
                    strategies[j].getClass().getSimpleName() + " should have different codes");
            }
        }
    }

    @Test
    void colorCodesMatchExpectedValues() {
        assertEquals(0, new TransparentColorStrategy().getColorCode());
        assertEquals(1, new AquaColorStrategy().getColorCode());
        assertEquals(2, new BlueVioletColorStrategy().getColorCode());
        assertEquals(3, new DarkGreenColorStrategy().getColorCode());
        assertEquals(4, new YellowColorStrategy().getColorCode());
        assertEquals(5, new RedColorStrategy().getColorCode());
        assertEquals(6, new BeigeColorStrategy().getColorCode());
        assertEquals(7, new BurlyWoodColorStrategy().getColorCode());
        assertEquals(-1, new WhiteColorStrategy().getColorCode());
    }
}

