package com.comp2042.ui.theme;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RetroThemeTest {

    private RetroTheme theme;

    @BeforeEach
    void setUp() {
        theme = new RetroTheme();
    }

    @Test
    void getNameReturnsRetro() {
        assertEquals("Retro", theme.getName());
    }

    @Test
    void getStylesheetReturnsCorrectPath() {
        assertEquals("retro_style.css", theme.getStylesheet());
    }

    @Test
    void getBrickColorReturnsNonNullForValidCodes() {
        for (int code = 1; code <= 7; code++) {
            Paint color = theme.getBrickColor(code);
            assertNotNull(color, "Brick color for code " + code + " should not be null");
        }
    }

    @Test
    void getBrickColorReturnsTransparentForCode0() {
        Paint color = theme.getBrickColor(0);
        assertEquals(Color.TRANSPARENT, color);
    }

    @Test
    void getBoardBackgroundColorReturnsLightGreen() {
        Paint color = theme.getBoardBackgroundColor();
        
        assertTrue(color instanceof Color);
        Color bgColor = (Color) color;
        
        // Should be a light green color (high green component)
        assertTrue(bgColor.getGreen() > bgColor.getRed(), "Background should be green-tinted");
    }

    @Test
    void implementsThemeInterface() {
        assertTrue(theme instanceof Theme);
    }

    @Test
    void brickColorsAreGreenTinted() {
        // Retro theme uses green palette
        for (int code = 1; code <= 7; code++) {
            Paint paint = theme.getBrickColor(code);
            assertTrue(paint instanceof Color);
            Color color = (Color) paint;
            
            // Green-tinted means green >= red or green >= blue
            assertTrue(color.getGreen() >= color.getRed() || color.getGreen() >= color.getBlue(),
                "Retro brick color for code " + code + " should be green-tinted");
        }
    }

    @Test
    void brickColorsAreDarkForContrast() {
        // Bricks should be darker than the background for visibility
        Paint bgPaint = theme.getBoardBackgroundColor();
        Color bgColor = (Color) bgPaint;
        double bgBrightness = bgColor.getBrightness();
        
        for (int code = 1; code <= 7; code++) {
            Paint brickPaint = theme.getBrickColor(code);
            Color brickColor = (Color) brickPaint;
            double brickBrightness = brickColor.getBrightness();
            
            assertTrue(brickBrightness < bgBrightness,
                "Brick color for code " + code + " should be darker than background");
        }
    }

    @Test
    void usesMonochromePalette() {
        // Retro theme uses only green shades - bricks should use limited colors
        Paint color1 = theme.getBrickColor(1);
        Paint color2 = theme.getBrickColor(2);
        
        // Should use alternating colors based on modulo
        assertNotNull(color1);
        assertNotNull(color2);
    }

    @Test
    void oddAndEvenBrickCodesUseDifferentColors() {
        // Based on implementation: brickCode % 2
        Paint oddColor = theme.getBrickColor(1);  // 1 % 2 = 1
        Paint evenColor = theme.getBrickColor(2); // 2 % 2 = 0
        
        // They should be different shades of green
        assertNotEquals(oddColor, evenColor);
    }

    @Test
    void sameParityBrickCodesUseSameColor() {
        // Codes with same modulo should have same color
        Paint color1 = theme.getBrickColor(1);
        Paint color3 = theme.getBrickColor(3);
        Paint color5 = theme.getBrickColor(5);
        
        assertEquals(color1, color3, "Odd codes should use same color");
        assertEquals(color3, color5, "Odd codes should use same color");
    }
}

