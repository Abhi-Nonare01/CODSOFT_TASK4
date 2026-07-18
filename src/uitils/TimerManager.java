package utils;

import javax.swing.Timer;
import java.util.function.Consumer;

/**
 * Utility class for managing the 30-second question countdown.
 */
public class TimerManager {
    private Timer timer;
    private int secondsRemaining;

    public void startTimer(int seconds, Consumer<Integer> onTick, Runnable onFinish) {
        stopTimer();
        this.secondsRemaining = seconds;

        // Initial tick update
        onTick.accept(secondsRemaining);

        timer = new Timer(1000, e -> {
            secondsRemaining--;
            onTick.accept(secondsRemaining);
            if (secondsRemaining <= 0) {
                stopTimer();
                onFinish.run();
            }
        });
        timer.start();
    }

    public void stopTimer() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
    }

    public int getSecondsRemaining() {
        return secondsRemaining;
    }
}