package app;

import quiz.QuizApplication;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.Dimension;

/**
 * Entry point for QuizMaster2026.
 * Sets up the main window frame and launches the Quiz Application dashboard.
 */
public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("CrossPlatformLookAndFeel not available. Proceeding with default.");
        }

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("QuizMaster 2026 - CodeSoft Java Internship");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setMinimumSize(new Dimension(900, 700));
            frame.setPreferredSize(new Dimension(1000, 750));

            QuizApplication app = new QuizApplication();
            frame.setContentPane(app);

            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}