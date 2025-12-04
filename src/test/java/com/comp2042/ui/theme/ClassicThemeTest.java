package com.comp2042.ui.theme;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClassicThemeTest {

    private ClassicTheme theme;

    @BeforeEach
    void setUp() {
        theme = new ClassicTheme();
    }

    @Test
    void getNameReturnsClassic() {
        assertEquals("Classic", theme.getName());
    }

    @Test
    void getStylesheetReturnsCorrectPath() {
        assertEquals("window_style.css", theme.getStylesheet());
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
    void getBoardBackgroundColorReturnsTransparent() {
        Paint color = theme.getBoardBackgroundColor();
        assertEquals(Color.TRANSPARENT, color);
    }

    @Test
    void implementsThemeInterface() {
        assertTrue(theme instanceof Theme);
    }

    @Test
    void getBrickColorReturnsConsistentResults() {
        Paint color1 = theme.getBrickColor(1);
        Paint color2 = theme.getBrickColor(1);
        
        assertEquals(color1, color2, "Same brick code should return same color");
    }

    @Test
    void differentBrickCodesReturnDifferentColors() {
        Paint color1 = theme.getBrickColor(1);
        Paint color2 = theme.getBrickColor(2);
        
        assertNotEquals(color1, color2, "Different brick codes should return different colors");
    }
}

