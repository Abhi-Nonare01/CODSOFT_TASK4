package quiz;

import model.Question;
import model.Result;
import ui.GlassPanel;
import ui.GradientPanel;
import ui.RoundedButton;
import ui.RoundedRadioButton;
import utils.ThemeManager;
import utils.TimerManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Main UI Router and Controller.
 * Handles Home, Quiz, and Result Screens using CardLayout.
 */
public class QuizApplication extends GradientPanel {
    private final CardLayout cardLayout;
    private final JPanel mainContainer;
    private final QuizEngine engine;
    private final TimerManager timerManager;
    private final int TIME_LIMIT = 30; // 30 seconds per question

    // UI Components for Home
    private JTextField nameField;

    // UI Components for Quiz
    private JLabel qCountLabel;
    private JLabel scoreLabel;
    private JLabel timerLabel;
    private JTextArea questionArea;
    private RoundedRadioButton[] optionButtons;
    private ButtonGroup optionGroup;
    private JProgressBar progressBar;

    public QuizApplication() {
        super();
        setLayout(new BorderLayout());

        engine = new QuizEngine();
        timerManager = new TimerManager();

        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);
        mainContainer.setOpaque(false);

        mainContainer.add(createHomeScreen(), "HOME");
        mainContainer.add(createQuizScreen(), "QUIZ");
        mainContainer.add(new JPanel(), "RESULT"); // Placeholder, generated dynamically

        add(mainContainer, BorderLayout.CENTER);
        cardLayout.show(mainContainer, "HOME");
    }

    /* =========================================
     * HOME SCREEN
     * ========================================= */
    private JPanel createHomeScreen() {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);

        GlassPanel card = new GlassPanel(30);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(50, 60, 50, 60));
        card.setPreferredSize(new Dimension(500, 450));

        JLabel logo = new JLabel("🧠", SwingConstants.CENTER);
        logo.setFont(new Font("Segoe UI", Font.PLAIN, 64));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("QuizMaster 2026");
        title.setFont(ThemeManager.getInstance().getDisplayFont());
        title.setForeground(ThemeManager.getInstance().getTextPrimary());
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Test your Java Knowledge!");
        subtitle.setFont(ThemeManager.getInstance().getNormalFont());
        subtitle.setForeground(ThemeManager.getInstance().getTextSecondary());
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        nameField = new JTextField();
        nameField.setMaximumSize(new Dimension(300, 40));
        nameField.setFont(ThemeManager.getInstance().getNormalFont());
        nameField.setHorizontalAlignment(JTextField.CENTER);
        nameField.setBackground(ThemeManager.getInstance().getInputBackground());
        nameField.setForeground(ThemeManager.getInstance().getTextPrimary());
        nameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ThemeManager.getInstance().getGlassBorder(), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        nameField.setCaretColor(ThemeManager.getInstance().getAccentColor());
        nameField.setAlignmentX(Component.CENTER_ALIGNMENT);

        RoundedButton startBtn = new RoundedButton("Start Quiz");
        startBtn.setMaximumSize(new Dimension(300, 45));
        startBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        startBtn.addActionListener(e -> startGame());

        RoundedButton themeBtn = new RoundedButton("Toggle Theme", new Color(71, 85, 105), new Color(100, 116, 139), 20);
        themeBtn.setMaximumSize(new Dimension(150, 35));
        themeBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        themeBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        themeBtn.addActionListener(e -> toggleTheme());

        card.add(logo);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(title);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(subtitle);
        card.add(Box.createRigidArea(new Dimension(0, 40)));

        JLabel nameLbl = new JLabel("Enter Player Name:");
        nameLbl.setForeground(ThemeManager.getInstance().getTextSecondary());
        nameLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(nameLbl);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(nameField);

        card.add(Box.createRigidArea(new Dimension(0, 30)));
        card.add(startBtn);
        card.add(Box.createRigidArea(new Dimension(0, 30)));
        card.add(themeBtn);

        wrapper.add(card);
        return wrapper;
    }

    private void startGame() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            name = "Guest Player";
        }
        engine.startQuiz(name, 10); // Start with 10 random questions
        loadQuestionData();
        cardLayout.show(mainContainer, "QUIZ");
    }

    /* =========================================
     * QUIZ SCREEN
     * ========================================= */
    private JPanel createQuizScreen() {
        JPanel wrapper = new JPanel(new BorderLayout(20, 20));
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(20, 40, 20, 40));

        // HEADER
        GlassPanel header = new GlassPanel(15, false);
        header.setLayout(new BorderLayout());
        header.setBorder(new EmptyBorder(15, 20, 15, 20));

        qCountLabel = new JLabel("Question 1 / 10");
        qCountLabel.setFont(ThemeManager.getInstance().getHeaderFont());
        qCountLabel.setForeground(ThemeManager.getInstance().getTextPrimary());

        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setFont(ThemeManager.getInstance().getSubHeaderFont());
        scoreLabel.setForeground(ThemeManager.getInstance().getAccentColor());

        timerLabel = new JLabel("⏱ 30s");
        timerLabel.setFont(ThemeManager.getInstance().getHeaderFont());
        timerLabel.setForeground(ThemeManager.getInstance().getWarningColor());

        JPanel headerRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        headerRight.setOpaque(false);
        headerRight.add(scoreLabel);
        headerRight.add(timerLabel);

        header.add(qCountLabel, BorderLayout.WEST);
        header.add(headerRight, BorderLayout.EAST);

        // BODY (Question & Options)
        GlassPanel body = new GlassPanel(20);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(new EmptyBorder(30, 40, 30, 40));

        questionArea = new JTextArea("Question Text Here");
        questionArea.setFont(ThemeManager.getInstance().getHeaderFont());
        questionArea.setForeground(ThemeManager.getInstance().getTextPrimary());
        questionArea.setOpaque(false);
        questionArea.setWrapStyleWord(true);
        questionArea.setLineWrap(true);
        questionArea.setEditable(false);
        questionArea.setFocusable(false);
        questionArea.setAlignmentX(Component.LEFT_ALIGNMENT);

        body.add(questionArea);
        body.add(Box.createRigidArea(new Dimension(0, 40)));

        optionButtons = new RoundedRadioButton[4];
        optionGroup = new ButtonGroup();
        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new RoundedRadioButton("Option " + (i + 1));
            optionButtons[i].setAlignmentX(Component.LEFT_ALIGNMENT);
            optionGroup.add(optionButtons[i]);
            body.add(optionButtons[i]);
            body.add(Box.createRigidArea(new Dimension(0, 15)));
        }

        // FOOTER
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);

        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(false);
        progressBar.setForeground(ThemeManager.getInstance().getAccentColor());
        progressBar.setBackground(ThemeManager.getInstance().getGlassBorder());
        progressBar.setBorderPainted(false);
        progressBar.setPreferredSize(new Dimension(0, 10));

        RoundedButton nextBtn = new RoundedButton("Submit & Next");
        nextBtn.setPreferredSize(new Dimension(200, 45));
        nextBtn.addActionListener(e -> processAnswer(getSelectedOptionIndex()));

        RoundedButton quitBtn = new RoundedButton("Quit Quiz", ThemeManager.getInstance().getErrorColor(), ThemeManager.getInstance().getErrorColor().brighter(), 20);
        quitBtn.setPreferredSize(new Dimension(150, 45));
        quitBtn.addActionListener(e -> handleQuit());

        JPanel footerBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        footerBtns.setOpaque(false);
        footerBtns.add(quitBtn);
        footerBtns.add(nextBtn);

        footer.add(progressBar, BorderLayout.NORTH);
        footer.add(footerBtns, BorderLayout.CENTER);

        wrapper.add(header, BorderLayout.NORTH);
        wrapper.add(body, BorderLayout.CENTER);
        wrapper.add(footer, BorderLayout.SOUTH);

        return wrapper;
    }

    private void loadQuestionData() {
        Question q = engine.getCurrentQuestion();
        if (q == null) return;

        qCountLabel.setText("Question " + engine.getCurrentQuestionNumber() + " / " + engine.getTotalQuestions());
        scoreLabel.setText("Score: " + engine.getCurrentScore());
        questionArea.setText(q.getQuestionText());

        String[] ops = q.getOptions();
        optionGroup.clearSelection();
        for (int i = 0; i < 4; i++) {
            optionButtons[i].setText(ops[i]);
        }

        int progress = (int) (((double) (engine.getCurrentQuestionNumber() - 1) / engine.getTotalQuestions()) * 100);
        progressBar.setValue(progress);

        startCountdown();
    }

    private void startCountdown() {
        timerManager.startTimer(TIME_LIMIT, seconds -> {
            timerLabel.setText("⏱ " + seconds + "s");
            if (seconds <= 10) {
                timerLabel.setForeground(ThemeManager.getInstance().getErrorColor());
            } else {
                timerLabel.setForeground(ThemeManager.getInstance().getWarningColor());
            }
        }, () -> {
            // Auto submit when time is up (skip)
            processAnswer(-1);
        });
    }

    private int getSelectedOptionIndex() {
        for (int i = 0; i < 4; i++) {
            if (optionButtons[i].isSelected()) return i;
        }
        return -1;
    }

    private void processAnswer(int selectedIndex) {
        timerManager.stopTimer();

        boolean correct = engine.submitAnswer(selectedIndex);

        if (engine.isFinished()) {
            showResultScreen();
        } else {
            loadQuestionData();
        }
    }

    private void handleQuit() {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to quit the quiz?", "Quit Quiz", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            timerManager.stopTimer();
            showResultScreen();
        }
    }

    /* =========================================
     * RESULT SCREEN
     * ========================================= */
    private void showResultScreen() {
        Result result = engine.getFinalResult();

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);

        GlassPanel card = new GlassPanel(30);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(40, 50, 40, 50));
        card.setPreferredSize(new Dimension(600, 500));

        JLabel title = new JLabel("Quiz Completed!");
        title.setFont(ThemeManager.getInstance().getDisplayFont());
        title.setForeground(ThemeManager.getInstance().getTextPrimary());
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel player = new JLabel("Player: " + result.getPlayerName());
        player.setFont(ThemeManager.getInstance().getSubHeaderFont());
        player.setForeground(ThemeManager.getInstance().getTextSecondary());
        player.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Status Ribbon
        JLabel status = new JLabel(result.getPassFailStatus() + " - " + result.getGrade());
        status.setFont(ThemeManager.getInstance().getHeaderFont());
        status.setForeground(result.getPassFailStatus().equals("PASS") ? ThemeManager.getInstance().getSuccessColor() : ThemeManager.getInstance().getErrorColor());
        status.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Stats Grid
        JPanel statsGrid = new JPanel(new GridLayout(2, 2, 20, 20));
        statsGrid.setOpaque(false);
        statsGrid.setMaximumSize(new Dimension(400, 150));

        statsGrid.add(createStatBox("Final Score", String.valueOf(result.getFinalScore()), ThemeManager.getInstance().getAccentColor()));
        statsGrid.add(createStatBox("Accuracy", String.format("%.1f %%", result.getPercentage()), ThemeManager.getInstance().getWarningColor()));
        statsGrid.add(createStatBox("Correct Answers", String.valueOf(result.getCorrectAnswers()), ThemeManager.getInstance().getSuccessColor()));
        statsGrid.add(createStatBox("Wrong/Skipped", (result.getWrongAnswers() + result.getSkippedQuestions()) + "", ThemeManager.getInstance().getErrorColor()));

        RoundedButton restartBtn = new RoundedButton("Play Again");
        restartBtn.setMaximumSize(new Dimension(200, 45));
        restartBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        restartBtn.addActionListener(e -> cardLayout.show(mainContainer, "HOME"));

        card.add(title);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(player);
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        card.add(status);
        card.add(Box.createRigidArea(new Dimension(0, 30)));
        card.add(statsGrid);
        card.add(Box.createRigidArea(new Dimension(0, 40)));
        card.add(restartBtn);

        wrapper.add(card);

        // Replace old result screen
        mainContainer.add(wrapper, "RESULT");
        cardLayout.show(mainContainer, "RESULT");
    }

    private JPanel createStatBox(String title, String value, Color valueColor) {
        GlassPanel p = new GlassPanel(15, false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel tLbl = new JLabel(title);
        tLbl.setFont(ThemeManager.getInstance().getNormalFont());
        tLbl.setForeground(ThemeManager.getInstance().getTextSecondary());
        tLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel vLbl = new JLabel(value);
        vLbl.setFont(ThemeManager.getInstance().getHeaderFont());
        vLbl.setForeground(valueColor);
        vLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        p.add(tLbl);
        p.add(vLbl);
        return p;
    }

    private void toggleTheme() {
        ThemeManager.getInstance().toggleTheme();
        SwingUtilities.updateComponentTreeUI(SwingUtilities.getWindowAncestor(this));
        // Force refresh custom painted components
        repaint();
    }
}