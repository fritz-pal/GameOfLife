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
                Thread.sleep(1000 - updateRate);
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

    public void setUpdateRate(int updateRate) {
        this.updateRate = updateRate;
    }
}

