package com.comp2042.ui.theme;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the active theme of the application.
 * Follows the Singleton pattern to provide global access to the current theme.
 * Uses the Observer pattern via JavaFX properties to notify listeners of theme changes.
 */
public final class ThemeManager {
    
    private static final ThemeManager INSTANCE = new ThemeManager();
    
    private final ObjectProperty<Theme> currentTheme;
    private final List<Theme> availableThemes;
    private int currentThemeIndex;

    private ThemeManager() {
        availableThemes = new ArrayList<>();
        currentTheme = new SimpleObjectProperty<>();
        
        // Register default themes
        registerTheme(new ClassicTheme());
        registerTheme(new NeonTheme());
        registerTheme(new RetroTheme());
    }

    /**
     * Gets the singleton instance of the ThemeManager.
     * 
     * @return the ThemeManager instance
     */
    public static ThemeManager getInstance() {
        return INSTANCE;
    }

    /**
     * Registers a theme and adds it to the rotation.
     * If it's the first theme, it becomes the active theme.
     * 
     * @param theme the theme to register
     */
    public void registerTheme(Theme theme) {
        availableThemes.add(theme);
        if (currentTheme.get() == null) {
            currentTheme.set(theme);
            currentThemeIndex = 0;
        }
    }

    /**
     * Cycles to the next available theme.
     */
    public void cycleTheme() {
        if (availableThemes.isEmpty()) return;
        
        currentThemeIndex = (currentThemeIndex + 1) % availableThemes.size();
        currentTheme.set(availableThemes.get(currentThemeIndex));
    }

    /**
     * Gets the current theme property.
     * UI components can bind to or listen to this property.
     * 
     * @return the ObjectProperty containing the current Theme
     */
    public ObjectProperty<Theme> currentThemeProperty() {
        return currentTheme;
    }

    /**
     * Gets the currently active theme.
     * 
     * @return the active Theme
     */
    public Theme getCurrentTheme() {
        return currentTheme.get();
    }
}

