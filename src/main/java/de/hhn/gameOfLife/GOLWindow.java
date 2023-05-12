package de.hhn.gameOfLife;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GOLWindow extends JInternalFrame {
    GamePanel gamePanel = new GamePanel();
    private GameThread gameThread = null;
    private int updateRate = 100;

    public GOLWindow(int rows, int columns) {
        super("Game of Life", true, true, true, true);
        this.setSize(1000, 900);
        this.setLayout(null);
        this.add(gamePanel);
        this.setFrameIcon(new ImageIcon("src/main/resources/glider.png"));

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

        JButton importButton = new JButton("Place Glider");
        importButton.setBounds(800, 250, 100, 50);
        importButton.addActionListener(e -> gamePanel.setFigure(importFigure("glider_gun.png")));
        this.add(importButton);


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

    private boolean[][] importFigure(String path) {
        File file = new File("src/main/resources/" + path);
        BufferedImage image;
        try {
            image = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
            return new boolean[0][0];
        }
        boolean[][] figure = new boolean[image.getHeight()][image.getWidth()];
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                figure[y][x] = image.getRGB(x, y) == Color.BLACK.getRGB();
            }
        }
        return figure;
    }
}
