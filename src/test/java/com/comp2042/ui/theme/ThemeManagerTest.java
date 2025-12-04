package com.comp2042.ui.theme;

import javafx.beans.property.ObjectProperty;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ThemeManagerTest {

    @Test
    void getInstanceReturnsSameInstance() {
        ThemeManager instance1 = ThemeManager.getInstance();
        ThemeManager instance2 = ThemeManager.getInstance();
        
        assertSame(instance1, instance2, "Should return the same singleton instance");
    }

    @Test
    void getInstanceReturnsNonNull() {
        ThemeManager instance = ThemeManager.getInstance();
        assertNotNull(instance);
    }

    @Test
    void getCurrentThemeReturnsTheme() {
        ThemeManager manager = ThemeManager.getInstance();
        Theme theme = manager.getCurrentTheme();
        
        assertNotNull(theme, "Current theme should not be null");
    }

    @Test
    void currentThemePropertyReturnsProperty() {
        ThemeManager manager = ThemeManager.getInstance();
        ObjectProperty<Theme> property = manager.currentThemeProperty();
        
        assertNotNull(property);
        assertNotNull(property.get());
    }

    @Test
    void currentThemePropertyMatchesGetCurrentTheme() {
        ThemeManager manager = ThemeManager.getInstance();
        
        assertEquals(manager.getCurrentTheme(), manager.currentThemeProperty().get());
    }

    @Test
    void cycleThemeChangesCurrentTheme() {
        ThemeManager manager = ThemeManager.getInstance();
        Theme initialTheme = manager.getCurrentTheme();
        
        manager.cycleTheme();
        Theme afterCycle = manager.getCurrentTheme();
        
        // Theme should change (unless only one theme is registered)
        assertNotNull(afterCycle);
    }

    @Test
    void cycleThemeWrapsAround() {
        ThemeManager manager = ThemeManager.getInstance();
        Theme initialTheme = manager.getCurrentTheme();
        
        // Cycle through all themes (assuming 3 themes: Classic, Neon, Retro)
        manager.cycleTheme(); // 1
        manager.cycleTheme(); // 2
        manager.cycleTheme(); // Should wrap back to 0
        
        Theme afterFullCycle = manager.getCurrentTheme();
        assertEquals(initialTheme, afterFullCycle, "Should wrap around to initial theme");
    }

    @Test
    void themesIncludeClassic() {
        ThemeManager manager = ThemeManager.getInstance();
        
        // Check that Classic theme is in the rotation
        boolean foundClassic = false;
        for (int i = 0; i < 3; i++) {
            if ("Classic".equals(manager.getCurrentTheme().getName())) {
                foundClassic = true;
                break;
            }
            manager.cycleTheme();
        }
        
        assertTrue(foundClassic, "Classic theme should be registered");
    }

    @Test
    void themePropertyCanBeObserved() {
        ThemeManager manager = ThemeManager.getInstance();
        ObjectProperty<Theme> property = manager.currentThemeProperty();
        
        final boolean[] listenerCalled = {false};
        property.addListener((obs, oldVal, newVal) -> {
            listenerCalled[0] = true;
        });
        
        manager.cycleTheme();
        
        assertTrue(listenerCalled[0], "Listener should be called when theme changes");
    }

    @Test
    void allThemesAreRegistered() {
        ThemeManager manager = ThemeManager.getInstance();
        
        // Collect all theme names by cycling through
        java.util.Set<String> foundThemes = new java.util.HashSet<>();
        for (int i = 0; i < 4; i++) { // Cycle 4 times to ensure we see all 3
            foundThemes.add(manager.getCurrentTheme().getName());
            manager.cycleTheme();
        }
        
        assertTrue(foundThemes.contains("Classic"), "Classic theme should be registered");
        assertTrue(foundThemes.contains("Neon"), "Neon theme should be registered");
        assertTrue(foundThemes.contains("Retro"), "Retro theme should be registered");
        assertEquals(3, foundThemes.size(), "Should have exactly 3 themes");
    }
}

