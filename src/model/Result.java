package model;

/**
 * Represents the final result and statistics of a quiz session.
 */
public class Result {
    private final String playerName;
    private final int totalQuestions;
    private final int correctAnswers;
    private final int wrongAnswers;
    private final int skippedQuestions;
    private final int finalScore;

    public Result(String playerName, int totalQuestions, int correctAnswers, int wrongAnswers, int skippedQuestions, int finalScore) {
        this.playerName = playerName;
        this.totalQuestions = totalQuestions;
        this.correctAnswers = correctAnswers;
        this.wrongAnswers = wrongAnswers;
        this.skippedQuestions = skippedQuestions;
        this.finalScore = finalScore;
    }

    public String getPlayerName() { return playerName; }
    public int getTotalQuestions() { return totalQuestions; }
    public int getCorrectAnswers() { return correctAnswers; }
    public int getWrongAnswers() { return wrongAnswers; }
    public int getSkippedQuestions() { return skippedQuestions; }
    public int getFinalScore() { return finalScore; }

    public double getPercentage() {
        if (totalQuestions == 0) return 0.0;
        return ((double) correctAnswers / totalQuestions) * 100.0;
    }

    public String getGrade() {
        double percentage = getPercentage();
        if (percentage >= 90) return "Excellent";
        if (percentage >= 75) return "Very Good";
        if (percentage >= 60) return "Good";
        if (percentage >= 40) return "Average";
        return "Needs Improvement";
    }

    public String getPassFailStatus() {
        return getPercentage() >= 40 ? "PASS" : "FAIL";
    }
}