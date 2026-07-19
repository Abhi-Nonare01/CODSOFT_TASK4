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
 * Features a 100% Custom Drawn Premium Logo (No Font/Emoji Dependencies).
 */
public class QuizApplication extends GradientPanel {
    private final CardLayout cardLayout;
    private final JPanel mainContainer;
    private final QuizEngine engine;
    private final TimerManager timerManager;
    private final int TIME_LIMIT = 30;

    private JTextField nameField;
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
        mainContainer.add(new JPanel(), "RESULT");

        add(mainContainer, BorderLayout.CENTER);
        cardLayout.show(mainContainer, "HOME");
    }

    private JPanel createHomeScreen() {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);

        GlassPanel card = new GlassPanel(30, true);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(50, 60, 50, 60));
        card.setPreferredSize(new Dimension(550, 500));

        // 100% CUSTOM DRAWN LOGO (No emojis, no square box errors)
        JPanel premiumLogo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw elegant gradient box
                GradientPaint gp = new GradientPaint(0, 0, ThemeManager.getInstance().getAccentColor(), 80, 80, ThemeManager.getInstance().getAccentHoverColor());
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, 80, 80, 25, 25);

                // Draw custom 'Q' inside
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("SansSerif", Font.BOLD, 48));
                FontMetrics fm = g2d.getFontMetrics();
                int textX = (80 - fm.stringWidth("Q")) / 2;
                int textY = ((80 - fm.getHeight()) / 2) + fm.getAscent();
                g2d.drawString("Q", textX, textY);
            }
        };
        premiumLogo.setPreferredSize(new Dimension(80, 80));
        premiumLogo.setMaximumSize(new Dimension(80, 80));
        premiumLogo.setOpaque(false);
        premiumLogo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("QuizMaster 2026");
        title.setFont(ThemeManager.getInstance().getDisplayFont());
        title.setForeground(ThemeManager.getInstance().getTextPrimary());
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("The Ultimate Developer Challenge");
        subtitle.setFont(ThemeManager.getInstance().getNormalFont());
        subtitle.setForeground(ThemeManager.getInstance().getTextSecondary());
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        nameField = new JTextField();
        nameField.setMaximumSize(new Dimension(350, 45));
        nameField.setFont(ThemeManager.getInstance().getHeaderFont());
        nameField.setHorizontalAlignment(JTextField.CENTER);
        nameField.setBackground(ThemeManager.getInstance().getInputBackground());
        nameField.setForeground(ThemeManager.getInstance().getTextPrimary());
        nameField.setCaretColor(ThemeManager.getInstance().getAccentColor());
        nameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ThemeManager.getInstance().getGlassBorder(), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        nameField.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Enter key support
        nameField.addActionListener(e -> startGame());

        RoundedButton startBtn = new RoundedButton("Start Quiz");
        startBtn.setMaximumSize(new Dimension(350, 45));
        startBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        startBtn.addActionListener(e -> startGame());

        RoundedButton themeBtn = new RoundedButton("Switch Theme", new Color(51, 65, 85), new Color(71, 85, 105));
        themeBtn.setMaximumSize(new Dimension(180, 40));
        themeBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        themeBtn.addActionListener(e -> toggleTheme());

        card.add(premiumLogo);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(title);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(subtitle);
        card.add(Box.createRigidArea(new Dimension(0, 40)));

        JLabel nameLbl = new JLabel("Enter Player Name (Press Enter to Start)");
        nameLbl.setFont(ThemeManager.getInstance().getNormalFont());
        nameLbl.setForeground(ThemeManager.getInstance().getTextSecondary());
        nameLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(nameLbl);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(nameField);

        card.add(Box.createRigidArea(new Dimension(0, 30)));
        card.add(startBtn);
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        card.add(themeBtn);

        wrapper.add(card);
        return wrapper;
    }

    private void startGame() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            name = "Guest Player";
        }
        engine.startQuiz(name, 10);
        loadQuestionData();
        cardLayout.show(mainContainer, "QUIZ");
    }

    /* =========================================
     * QUIZ SCREEN
     * ========================================= */
    private JPanel createQuizScreen() {
        JPanel wrapper = new JPanel(new BorderLayout(20, 20));
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(30, 50, 30, 50));

        // HEADER
        GlassPanel header = new GlassPanel(20, false);
        header.setLayout(new BorderLayout());
        header.setBorder(new EmptyBorder(20, 30, 20, 30));

        qCountLabel = new JLabel("Question 1 / 10");
        qCountLabel.setFont(ThemeManager.getInstance().getHeaderFont());
        qCountLabel.setForeground(ThemeManager.getInstance().getTextPrimary());

        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setFont(ThemeManager.getInstance().getSubHeaderFont());
        scoreLabel.setForeground(ThemeManager.getInstance().getAccentColor());

        timerLabel = new JLabel("Time: 30s");
        timerLabel.setFont(ThemeManager.getInstance().getHeaderFont());
        timerLabel.setForeground(ThemeManager.getInstance().getWarningColor());

        JPanel headerRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 30, 0));
        headerRight.setOpaque(false);
        headerRight.add(scoreLabel);
        headerRight.add(timerLabel);

        header.add(qCountLabel, BorderLayout.WEST);
        header.add(headerRight, BorderLayout.EAST);

        // BODY
        JPanel bodyWrapper = new JPanel(new BorderLayout());
        bodyWrapper.setOpaque(false);

        questionArea = new JTextArea("Question Text Here");
        questionArea.setFont(ThemeManager.getInstance().getHeaderFont());
        questionArea.setForeground(ThemeManager.getInstance().getTextPrimary());
        questionArea.setOpaque(false);
        questionArea.setWrapStyleWord(true);
        questionArea.setLineWrap(true);
        questionArea.setEditable(false);
        questionArea.setFocusable(false);
        questionArea.setBorder(new EmptyBorder(20, 0, 30, 0));

        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        optionsPanel.setOpaque(false);

        optionButtons = new RoundedRadioButton[4];
        optionGroup = new ButtonGroup();
        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new RoundedRadioButton("Option " + (i + 1));
            optionGroup.add(optionButtons[i]);
            optionsPanel.add(optionButtons[i]);
            optionsPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        }

        bodyWrapper.add(questionArea, BorderLayout.NORTH);
        bodyWrapper.add(optionsPanel, BorderLayout.CENTER);

        // FOOTER
        JPanel footer = new JPanel(new BorderLayout(0, 20));
        footer.setOpaque(false);

        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(false);
        progressBar.setForeground(ThemeManager.getInstance().getAccentColor());
        progressBar.setBackground(ThemeManager.getInstance().getGlassBorder());
        progressBar.setBorderPainted(false);
        progressBar.setPreferredSize(new Dimension(0, 8));

        RoundedButton nextBtn = new RoundedButton("Submit Answer");
        nextBtn.setPreferredSize(new Dimension(250, 50));
        nextBtn.addActionListener(e -> processAnswer(getSelectedOptionIndex()));

        RoundedButton quitBtn = new RoundedButton("Quit", ThemeManager.getInstance().getErrorColor(), ThemeManager.getInstance().getErrorColor().brighter());
        quitBtn.setPreferredSize(new Dimension(150, 50));
        quitBtn.addActionListener(e -> handleQuit());

        JPanel footerBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        footerBtns.setOpaque(false);
        footerBtns.add(quitBtn);
        footerBtns.add(nextBtn);

        footer.add(progressBar, BorderLayout.NORTH);
        footer.add(footerBtns, BorderLayout.CENTER);

        wrapper.add(header, BorderLayout.NORTH);
        wrapper.add(bodyWrapper, BorderLayout.CENTER);
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
            timerLabel.setText("Time: " + seconds + "s");
            if (seconds <= 10) {
                timerLabel.setForeground(ThemeManager.getInstance().getErrorColor());
            } else {
                timerLabel.setForeground(ThemeManager.getInstance().getWarningColor());
            }
        }, () -> {
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
        engine.submitAnswer(selectedIndex);

        if (engine.isFinished()) {
            showResultScreen();
        } else {
            loadQuestionData();
        }
    }

    private void handleQuit() {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to quit?", "Quit Quiz", JOptionPane.YES_NO_OPTION);
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
        card.setBorder(new EmptyBorder(50, 60, 50, 60));
        card.setPreferredSize(new Dimension(650, 550));

        JLabel title = new JLabel("Quiz Completed");
        title.setFont(ThemeManager.getInstance().getDisplayFont());
        title.setForeground(ThemeManager.getInstance().getTextPrimary());
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel player = new JLabel("Player: " + result.getPlayerName());
        player.setFont(ThemeManager.getInstance().getSubHeaderFont());
        player.setForeground(ThemeManager.getInstance().getTextSecondary());
        player.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel status = new JLabel(result.getPassFailStatus() + " - " + result.getGrade());
        status.setFont(ThemeManager.getInstance().getHeaderFont());
        status.setForeground(result.getPassFailStatus().equals("PASS") ? ThemeManager.getInstance().getSuccessColor() : ThemeManager.getInstance().getErrorColor());
        status.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel statsGrid = new JPanel(new GridLayout(2, 2, 25, 25));
        statsGrid.setOpaque(false);
        statsGrid.setMaximumSize(new Dimension(500, 200));

        statsGrid.add(createStatBox("Total Score", String.valueOf(result.getFinalScore()), ThemeManager.getInstance().getAccentColor()));
        statsGrid.add(createStatBox("Accuracy", String.format("%.1f %%", result.getPercentage()), ThemeManager.getInstance().getWarningColor()));
        statsGrid.add(createStatBox("Correct", String.valueOf(result.getCorrectAnswers()), ThemeManager.getInstance().getSuccessColor()));
        statsGrid.add(createStatBox("Wrong/Skip", (result.getWrongAnswers() + result.getSkippedQuestions()) + "", ThemeManager.getInstance().getErrorColor()));

        RoundedButton restartBtn = new RoundedButton("Play Again");
        restartBtn.setMaximumSize(new Dimension(250, 50));
        restartBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        restartBtn.addActionListener(e -> cardLayout.show(mainContainer, "HOME"));

        card.add(title);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(player);
        card.add(Box.createRigidArea(new Dimension(0, 25)));
        card.add(status);
        card.add(Box.createRigidArea(new Dimension(0, 35)));
        card.add(statsGrid);
        card.add(Box.createRigidArea(new Dimension(0, 45)));
        card.add(restartBtn);

        wrapper.add(card);

        mainContainer.add(wrapper, "RESULT");
        cardLayout.show(mainContainer, "RESULT");
    }

    private JPanel createStatBox(String title, String value, Color valueColor) {
        GlassPanel p = new GlassPanel(20, false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel tLbl = new JLabel(title);
        tLbl.setFont(ThemeManager.getInstance().getNormalFont());
        tLbl.setForeground(ThemeManager.getInstance().getTextSecondary());
        tLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel vLbl = new JLabel(value);
        vLbl.setFont(ThemeManager.getInstance().getHeaderFont());
        vLbl.setForeground(valueColor);
        vLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        p.add(tLbl);
        p.add(Box.createRigidArea(new Dimension(0,5)));
        p.add(vLbl);
        return p;
    }

    private void toggleTheme() {
        ThemeManager.getInstance().toggleTheme();
        SwingUtilities.updateComponentTreeUI(SwingUtilities.getWindowAncestor(this));
        repaint();
    }
}