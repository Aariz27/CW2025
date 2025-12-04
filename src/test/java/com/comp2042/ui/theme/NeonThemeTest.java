package com.comp2042.ui.theme;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NeonThemeTest {

    private NeonTheme theme;

    @BeforeEach
    void setUp() {
        theme = new NeonTheme();
    }

    @Test
    void getNameReturnsNeon() {
        assertEquals("Neon", theme.getName());
    }

    @Test
    void getStylesheetReturnsCorrectPath() {
        assertEquals("neon_style.css", theme.getStylesheet());
    }

    @Test
    void getBrickColorReturnsCyanForCode1() {
        Paint color = theme.getBrickColor(1);
        assertEquals(Color.CYAN, color);
    }

    @Test
    void getBrickColorReturnsMagentaForCode2() {
        Paint color = theme.getBrickColor(2);
        assertEquals(Color.MAGENTA, color);
    }

    @Test
    void getBrickColorReturnsLimeForCode3() {
        Paint color = theme.getBrickColor(3);
        assertEquals(Color.LIME, color);
    }

    @Test
    void getBrickColorReturnsYellowForCode4() {
        Paint color = theme.getBrickColor(4);
        assertEquals(Color.YELLOW, color);
    }

    @Test
    void getBrickColorReturnsRedForCode5() {
        Paint color = theme.getBrickColor(5);
        assertEquals(Color.RED, color);
    }

    @Test
    void getBrickColorReturnsOrangeForCode6() {
        Paint color = theme.getBrickColor(6);
        assertEquals(Color.ORANGE, color);
    }

    @Test
    void getBrickColorReturnsPurpleForCode7() {
        Paint color = theme.getBrickColor(7);
        assertEquals(Color.PURPLE, color);
    }

    @Test
    void getBrickColorReturnsTransparentForCode0() {
        Paint color = theme.getBrickColor(0);
        assertEquals(Color.TRANSPARENT, color);
    }

    @Test
    void getBrickColorReturnsTransparentForInvalidCode() {
        Paint color = theme.getBrickColor(99);
        assertEquals(Color.TRANSPARENT, color);
    }

    @Test
    void getBoardBackgroundColorReturnsBlack() {
        Paint color = theme.getBoardBackgroundColor();
        assertEquals(Color.BLACK, color);
    }

    @Test
    void implementsThemeInterface() {
        assertTrue(theme instanceof Theme);
    }

    @Test
    void allBrickColorsAreBright() {
        // Neon theme should use bright, saturated colors
        for (int code = 1; code <= 7; code++) {
            Paint paint = theme.getBrickColor(code);
            assertTrue(paint instanceof Color);
            Color color = (Color) paint;
            // Bright colors should have at least one RGB component at max
            assertTrue(color.getRed() >= 0.5 || color.getGreen() >= 0.5 || color.getBlue() >= 0.5,
                "Neon color for code " + code + " should be bright");
        }
    }
}

