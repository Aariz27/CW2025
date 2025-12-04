package com.comp2042.ui;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ColorMapperTest {

    @Test
    void getFillColorReturnsTransparentForCode0() {
        Paint color = ColorMapper.getFillColor(0);
        assertEquals(Color.TRANSPARENT, color);
    }

    @Test
    void getFillColorReturnsAquaForCode1() {
        Paint color = ColorMapper.getFillColor(1);
        assertEquals(Color.AQUA, color);
    }

    @Test
    void getFillColorReturnsBlueVioletForCode2() {
        Paint color = ColorMapper.getFillColor(2);
        assertEquals(Color.BLUEVIOLET, color);
    }

    @Test
    void getFillColorReturnsDarkGreenForCode3() {
        Paint color = ColorMapper.getFillColor(3);
        assertEquals(Color.DARKGREEN, color);
    }

    @Test
    void getFillColorReturnsYellowForCode4() {
        Paint color = ColorMapper.getFillColor(4);
        assertEquals(Color.YELLOW, color);
    }

    @Test
    void getFillColorReturnsRedForCode5() {
        Paint color = ColorMapper.getFillColor(5);
        assertEquals(Color.RED, color);
    }

    @Test
    void getFillColorReturnsBeigeForCode6() {
        Paint color = ColorMapper.getFillColor(6);
        assertEquals(Color.BEIGE, color);
    }

    @Test
    void getFillColorReturnsBurlyWoodForCode7() {
        Paint color = ColorMapper.getFillColor(7);
        assertEquals(Color.BURLYWOOD, color);
    }

    @Test
    void getFillColorReturnsWhiteForUnknownCode() {
        Paint color = ColorMapper.getFillColor(99);
        assertEquals(Color.WHITE, color);
    }

    @Test
    void getFillColorReturnsWhiteForNegativeCode() {
        Paint color = ColorMapper.getFillColor(-5);
        assertEquals(Color.WHITE, color);
    }

    @Test
    void getGhostFillColorReturnsSemiTransparentColor() {
        Paint ghostColor = ColorMapper.getGhostFillColor(1);
        
        assertTrue(ghostColor instanceof Color);
        Color color = (Color) ghostColor;
        assertEquals(0.3, color.getOpacity(), 0.01);
    }

    @Test
    void getGhostFillColorPreservesRGB() {
        Paint ghostColor = ColorMapper.getGhostFillColor(1);
        
        assertTrue(ghostColor instanceof Color);
        Color color = (Color) ghostColor;
        
        // Aqua is (0, 1, 1) in RGB
        assertEquals(Color.AQUA.getRed(), color.getRed(), 0.01);
        assertEquals(Color.AQUA.getGreen(), color.getGreen(), 0.01);
        assertEquals(Color.AQUA.getBlue(), color.getBlue(), 0.01);
    }

    @Test
    void getGhostFillColorReturnsTransparentForCode0() {
        Paint ghostColor = ColorMapper.getGhostFillColor(0);
        
        // Transparent should stay transparent
        assertTrue(ghostColor instanceof Color);
        Color color = (Color) ghostColor;
        assertEquals(0.3, color.getOpacity(), 0.01);
    }

    @Test
    void getGhostFillColorReturnsColorForAllValidCodes() {
        for (int code = 0; code <= 7; code++) {
            Paint ghostColor = ColorMapper.getGhostFillColor(code);
            assertNotNull(ghostColor);
            assertTrue(ghostColor instanceof Color);
        }
    }

    @Test
    void getGhostFillColorHasConsistentOpacity() {
        for (int code = 1; code <= 7; code++) {
            Paint ghostColor = ColorMapper.getGhostFillColor(code);
            Color color = (Color) ghostColor;
            assertEquals(0.3, color.getOpacity(), 0.01, 
                "Ghost color for code " + code + " should have 0.3 opacity");
        }
    }

    @Test
    void getFillColorReturnsNotNullForAllValidCodes() {
        for (int code = 0; code <= 7; code++) {
            Paint color = ColorMapper.getFillColor(code);
            assertNotNull(color, "Color for code " + code + " should not be null");
        }
    }
}

