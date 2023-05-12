package de.hhn.gameOfLife;

public class GameThread extends Thread {
    private final GamePanel window;
    private boolean running = true;
    private int updateRate;

    public GameThread(GamePanel window, int updateRate) {
        this.window = window;
        this.updateRate = updateRate;
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(calcSleepTime());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (window.getMode() == Mode.RUN)
                window.update();
        }
    }

    public void dispose() {
        running = false;
    }

    private int calcSleepTime() {
        return Math.max(1, (int) ((1000f / (0.01f * (((float) updateRate) + 92f))) - 91.575091575f));
    }

    public void setUpdateRate(int updateRate) {
        this.updateRate = updateRate;
    }
}

