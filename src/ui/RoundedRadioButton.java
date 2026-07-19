package ui;

import utils.ThemeManager;

import javax.swing.JRadioButton;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.FontMetrics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 2026 Trend: Replaces standard tiny radio buttons with Premium Full-Width Option Cards.
 * Gives a highly satisfying hover and selection glow effect.
 */
public class RoundedRadioButton extends JRadioButton {
    private boolean isHovered = false;
    private final int cornerRadius = 20;

    public RoundedRadioButton(String text) {
        super(text);
        setOpaque(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setFont(ThemeManager.getInstance().getNormalFont());

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { isHovered = true; repaint(); }
            @Override
            public void mouseExited(MouseEvent e) { isHovered = false; repaint(); }
        });
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(Integer.MAX_VALUE, 65); // Full width, tall card
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(Integer.MAX_VALUE, 65);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        ThemeManager theme = ThemeManager.getInstance();

        // Background logic
        if (isSelected()) {
            g2d.setColor(new Color(theme.getAccentColor().getRed(), theme.getAccentColor().getGreen(), theme.getAccentColor().getBlue(), 30));
        } else if (isHovered) {
            g2d.setColor(theme.getGlassColor());
        } else {
            g2d.setColor(new Color(0, 0, 0, 50));
        }
        g2d.fillRoundRect(0, 0, getWidth() - 2, getHeight() - 2, cornerRadius, cornerRadius);

        // Border logic
        if (isSelected()) {
            g2d.setColor(theme.getAccentColor());
            g2d.setStroke(new java.awt.BasicStroke(2.0f));
        } else if (isHovered) {
            g2d.setColor(theme.getGlassBorder());
            g2d.setStroke(new java.awt.BasicStroke(1.5f));
        } else {
            g2d.setColor(new Color(255, 255, 255, 20));
            g2d.setStroke(new java.awt.BasicStroke(1.0f));
        }
        g2d.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, cornerRadius, cornerRadius);

        // Indicator Dot
        int dotSize = 16;
        int dotY = (getHeight() - dotSize) / 2;
        if (isSelected()) {
            g2d.setColor(theme.getAccentColor());
            g2d.fillOval(20, dotY, dotSize, dotSize);
        } else {
            g2d.setColor(theme.getTextSecondary());
            g2d.drawOval(20, dotY, dotSize, dotSize);
        }

        // Text
        if (getText() != null) {
            g2d.setColor(isSelected() ? theme.getAccentColor() : theme.getTextPrimary());
            g2d.setFont(isSelected() ? ThemeManager.getInstance().getBoldFont() : getFont());
            FontMetrics fm = g2d.getFontMetrics();
            int textY = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
            g2d.drawString(getText(), 50, textY); // Padding from the dot
        }

        g2d.dispose();
    }
}