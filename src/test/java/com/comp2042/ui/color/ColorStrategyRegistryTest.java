package com.comp2042.ui.color;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ColorStrategyRegistryTest {

    @Test
    void getColorReturnsTransparentForCode0() {
        Paint color = ColorStrategyRegistry.getColor(0);
        assertEquals(Color.TRANSPARENT, color);
    }

    @Test
    void getColorReturnsAquaForCode1() {
        Paint color = ColorStrategyRegistry.getColor(1);
        assertEquals(Color.AQUA, color);
    }

    @Test
    void getColorReturnsBlueVioletForCode2() {
        Paint color = ColorStrategyRegistry.getColor(2);
        assertEquals(Color.BLUEVIOLET, color);
    }

    @Test
    void getColorReturnsDarkGreenForCode3() {
        Paint color = ColorStrategyRegistry.getColor(3);
        assertEquals(Color.DARKGREEN, color);
    }

    @Test
    void getColorReturnsYellowForCode4() {
        Paint color = ColorStrategyRegistry.getColor(4);
        assertEquals(Color.YELLOW, color);
    }

    @Test
    void getColorReturnsRedForCode5() {
        Paint color = ColorStrategyRegistry.getColor(5);
        assertEquals(Color.RED, color);
    }

    @Test
    void getColorReturnsBeigeForCode6() {
        Paint color = ColorStrategyRegistry.getColor(6);
        assertEquals(Color.BEIGE, color);
    }

    @Test
    void getColorReturnsBurlyWoodForCode7() {
        Paint color = ColorStrategyRegistry.getColor(7);
        assertEquals(Color.BURLYWOOD, color);
    }

    @Test
    void getColorReturnsWhiteForUnregisteredCode() {
        Paint color = ColorStrategyRegistry.getColor(100);
        assertEquals(Color.WHITE, color);
    }

    @Test
    void getColorReturnsWhiteForNegativeCode() {
        Paint color = ColorStrategyRegistry.getColor(-1);
        assertEquals(Color.WHITE, color);
    }

    @Test
    void getColorReturnsWhiteForLargeCode() {
        Paint color = ColorStrategyRegistry.getColor(Integer.MAX_VALUE);
        assertEquals(Color.WHITE, color);
    }

    @Test
    void getColorReturnsConsistentResultsForSameCode() {
        Paint first = ColorStrategyRegistry.getColor(3);
        Paint second = ColorStrategyRegistry.getColor(3);
        
        assertEquals(first, second);
    }

    @Test
    void allRegisteredCodesReturnNonNull() {
        for (int code = 0; code <= 7; code++) {
            Paint color = ColorStrategyRegistry.getColor(code);
            assertNotNull(color, "Color for code " + code + " should not be null");
        }
    }

    @Test
    void registeredColorsAreDistinct() {
        Paint[] colors = new Paint[8];
        for (int code = 0; code <= 7; code++) {
            colors[code] = ColorStrategyRegistry.getColor(code);
        }
        
        // Check that colors are distinct (except potentially transparent)
        for (int i = 1; i < colors.length; i++) {
            for (int j = i + 1; j < colors.length; j++) {
                assertNotEquals(colors[i], colors[j], 
                    "Colors for code " + i + " and " + j + " should be different");
            }
        }
    }
}

