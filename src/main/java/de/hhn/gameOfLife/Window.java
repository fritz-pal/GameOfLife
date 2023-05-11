package de.hhn.gameOfLife;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {
    GamePanel gamePanel = new GamePanel();
    private GameThread gameThread = null;
    private int updateRate = 100;

    public Window() {
        this.setSize(1000, 1000);
        this.setPreferredSize(new Dimension(1000, 1000));
        this.pack();
        this.setLayout(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.add(gamePanel);


        JButton startButton = new JButton("Start/Stop");
        startButton.setBounds(800, 0, 100, 50);
        startButton.addActionListener(e -> {
            if (gameThread == null) {
                gameThread = new GameThread(gamePanel, updateRate);
                gameThread.start();
            } else {
                gameThread.dispose();
                gameThread = null;
            }
        });
        this.add(startButton);

        JButton stepButton = new JButton("Step");
        stepButton.setBounds(800, 50, 100, 50);
        stepButton.addActionListener(e -> gamePanel.update());
        this.add(stepButton);

        JButton stepBackButton = new JButton("Step Back");
        stepBackButton.setBounds(800, 100, 100, 50);
        stepBackButton.addActionListener(e -> gamePanel.loadLastStep());
        this.add(stepBackButton);

        JButton clearButton = new JButton("Clear");
        clearButton.setBounds(800, 150, 100, 50);
        clearButton.addActionListener(e -> gamePanel.clear());
        this.add(clearButton);

        JButton randomButton = new JButton("Randomize");
        randomButton.setBounds(800, 200, 100, 50);
        randomButton.addActionListener(e -> gamePanel.randomize());
        this.add(randomButton);

        JSlider updateRateSlider = new JSlider(1, 1000, 800);
        updateRateSlider.setBounds(0, 800, 800, 50);
        updateRateSlider.addChangeListener(e -> {
            if (gameThread != null) {
                gameThread.setUpdateRate(updateRateSlider.getValue());
            }
            updateRate = updateRateSlider.getValue();
        });
        this.add(updateRateSlider);


        this.setVisible(true);
    }
}
