package utils;

import java.awt.Color;
import java.awt.Font;

/**
 * Premium 2026 Glassmorphism Theme.
 * Deep Purple and Slate aesthetics with Indigo Accents.
 */
public class ThemeManager {
    private static ThemeManager instance;
    private boolean darkMode = true;

    // Dark Mode (Deep Slate & Purple)
    private final Color darkGradientStart = new Color(15, 23, 42);
    private final Color darkGradientEnd = new Color(76, 29, 149);
    private final Color darkGlassColor = new Color(255, 255, 255, 15);
    private final Color darkGlassBorder = new Color(255, 255, 255, 30);
    private final Color darkTextPrimary = new Color(248, 250, 252);
    private final Color darkTextSecondary = new Color(148, 163, 184);
    private final Color darkInputBackground = new Color(30, 41, 59, 180);

    // Light Mode (Frost White & Lavender)
    private final Color lightGradientStart = new Color(241, 245, 249);
    private final Color lightGradientEnd = new Color(216, 180, 254);
    private final Color lightGlassColor = new Color(255, 255, 255, 200);
    private final Color lightGlassBorder = new Color(255, 255, 255, 255);
    private final Color lightTextPrimary = new Color(15, 23, 42);
    private final Color lightTextSecondary = new Color(71, 85, 105);
    private final Color lightInputBackground = new Color(255, 255, 255, 220);

    // Premium Accents (Indigo & Emerald)
    private final Color accentColor = new Color(99, 102, 241);
    private final Color accentHoverColor = new Color(79, 70, 229);
    private final Color successColor = new Color(16, 185, 129);
    private final Color errorColor = new Color(239, 68, 68);
    private final Color warningColor = new Color(245, 158, 11);

    // Modern Typography
    private final Font displayFont = new Font("Segoe UI", Font.BOLD, 46);
    private final Font headerFont = new Font("Segoe UI", Font.BOLD, 24);
    private final Font subHeaderFont = new Font("Segoe UI", Font.BOLD, 16);
    private final Font normalFont = new Font("Segoe UI", Font.PLAIN, 16);
    private final Font boldFont = new Font("Segoe UI", Font.BOLD, 16);

    private ThemeManager() {}

    public static ThemeManager getInstance() {
        if (instance == null) {
            instance = new ThemeManager();
        }
        return instance;
    }

    public void toggleTheme() { darkMode = !darkMode; }
    public boolean isDarkMode() { return darkMode; }

    public Color getGradientStart() { return darkMode ? darkGradientStart : lightGradientStart; }
    public Color getGradientEnd() { return darkMode ? darkGradientEnd : lightGradientEnd; }
    public Color getGlassColor() { return darkMode ? darkGlassColor : lightGlassColor; }
    public Color getGlassBorder() { return darkMode ? darkGlassBorder : lightGlassBorder; }
    public Color getTextPrimary() { return darkMode ? darkTextPrimary : lightTextPrimary; }
    public Color getTextSecondary() { return darkMode ? darkTextSecondary : lightTextSecondary; }
    public Color getInputBackground() { return darkMode ? darkInputBackground : lightInputBackground; }

    public Color getAccentColor() { return accentColor; }
    public Color getAccentHoverColor() { return accentHoverColor; }
    public Color getSuccessColor() { return successColor; }
    public Color getErrorColor() { return errorColor; }
    public Color getWarningColor() { return warningColor; }

    public Font getDisplayFont() { return displayFont; }
    public Font getHeaderFont() { return headerFont; }
    public Font getSubHeaderFont() { return subHeaderFont; }
    public Font getNormalFont() { return normalFont; }
    public Font getBoldFont() { return boldFont; }
}