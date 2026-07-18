package quiz;

import model.Question;
import model.Result;

import java.util.List;

/**
 * Core Logic Engine for the Quiz Application.
 * Manages states, scoring, and question iterations.
 */
public class QuizEngine {
    private String playerName;
    private List<Question> currentQuizQuestions;
    private int currentQuestionIndex;
    private int score;
    private int correctCount;
    private int wrongCount;
    private int skippedCount;

    public void startQuiz(String playerName, int questionCount) {
        this.playerName = playerName;
        QuestionBank bank = new QuestionBank();
        this.currentQuizQuestions = bank.getRandomQuestions(questionCount);
        this.currentQuestionIndex = 0;
        this.score = 0;
        this.correctCount = 0;
        this.wrongCount = 0;
        this.skippedCount = 0;
    }

    public Question getCurrentQuestion() {
        if (currentQuestionIndex < currentQuizQuestions.size()) {
            return currentQuizQuestions.get(currentQuestionIndex);
        }
        return null;
    }

    public int getCurrentQuestionNumber() {
        return currentQuestionIndex + 1;
    }

    public int getTotalQuestions() {
        return currentQuizQuestions.size();
    }

    public int getCurrentScore() {
        return score;
    }

    /**
     * Submits an answer.
     * @param selectedIndex The selected option index, or -1 if skipped/timeout.
     * @return true if correct, false otherwise.
     */
    public boolean submitAnswer(int selectedIndex) {
        Question q = getCurrentQuestion();
        boolean isCorrect = false;

        if (selectedIndex == -1) {
            skippedCount++;
        } else if (q.isCorrect(selectedIndex)) {
            score += 10;
            correctCount++;
            isCorrect = true;
        } else {
            wrongCount++;
        }

        currentQuestionIndex++;
        return isCorrect;
    }

    public boolean isFinished() {
        return currentQuestionIndex >= currentQuizQuestions.size();
    }

    public Result getFinalResult() {
        return new Result(playerName, getTotalQuestions(), correctCount, wrongCount, skippedCount, score);
    }
}