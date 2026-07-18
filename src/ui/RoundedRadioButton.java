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
 * A custom radio button designed for modern UI, avoiding standard OS look.
 * Fixes text clipping by calculating the exact preferred size for custom graphics.
 */
public class RoundedRadioButton extends JRadioButton {
    private boolean isHovered = false;
    private final int circleSize = 20;
    private final int gap = 15;

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

    /**
     * Tells the Layout Manager exactly how much space this custom component needs.
     */
    @Override
    public Dimension getPreferredSize() {
        FontMetrics fm = getFontMetrics(getFont());
        String text = getText() != null ? getText() : "";
        int textWidth = fm.stringWidth(text);

        // Width = circle + gap + text width + extra padding to avoid clipping
        int width = circleSize + gap + textWidth + 20;
        int height = Math.max(circleSize, fm.getHeight()) + 10;

        return new Dimension(width, height);
    }

    /**
     * Allows the component to stretch horizontally in BoxLayout if needed.
     */
    @Override
    public Dimension getMaximumSize() {
        return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        ThemeManager theme = ThemeManager.getInstance();
        int yOffset = (getHeight() - circleSize) / 2;

        // Draw Outer Circle
        g2d.setColor(isHovered ? theme.getAccentColor() : theme.getTextSecondary());
        g2d.drawOval(0, yOffset, circleSize, circleSize);

        // Draw Inner Circle if Selected
        if (isSelected()) {
            g2d.setColor(theme.getAccentColor());
            g2d.fillOval(4, yOffset + 4, circleSize - 8, circleSize - 8);
        }

        // Draw Text
        if (getText() != null) {
            g2d.setColor(theme.getTextPrimary());
            g2d.setFont(getFont());
            FontMetrics fm = g2d.getFontMetrics();
            int textY = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
            g2d.drawString(getText(), circleSize + gap, textY);
        }

        g2d.dispose();
    }
}