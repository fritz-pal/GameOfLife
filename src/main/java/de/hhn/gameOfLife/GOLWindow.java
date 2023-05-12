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
    private ImageIcon figureImage = null;
    JLabel imageLabel = new JLabel();
    JMenuBar menuGolWindow = new JMenuBar();

    public GOLWindow(int rows, int columns) {
        super("Game of Life", true, true, true, true);
        this.setSize(1000, 900);
        this.setLayout(null);
        this.add(gamePanel);
        this.setFrameIcon(new ImageIcon("src/main/resources/glider.png"));
        JMenu[] golMenus = {new JMenu("Start"), new JMenu("Stop"), new JMenu("Clear"), new JMenu("Step back"), new JMenu("Randomize"), new JMenu("Insert Figure")};
        for (JMenu m : golMenus) menuGolWindow.add(m);
        this.setJMenuBar(menuGolWindow);


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
        importButton.addActionListener(e -> importFigure("glider_gun.png"));
        this.add(importButton);

        imageLabel.setBounds(800, 300, 200, 200);
        imageLabel.setIcon(figureImage);
        this.add(imageLabel);

        JSlider updateRateSlider = new JSlider(1, 1000, 800);
        updateRateSlider.setBounds(0, 800, 800, 50);
        updateRateSlider.addChangeListener(e -> {
            if (gameThread != null) {
                gameThread.setUpdateRate(updateRateSlider.getValue());
            }
            updateRate = updateRateSlider.getValue();
        });
        this.add(updateRateSlider);

        importFigure("pulsar.png");

        this.setVisible(true);
    }

    private void importFigure(String path) {
        File file = new File("src/main/resources/" + path);
        BufferedImage image;
        try {
            image = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        figureImage = new ImageIcon(image.getScaledInstance(image.getWidth()*4, image.getHeight()*4, Image.SCALE_SMOOTH));
        imageLabel.setIcon(figureImage);

        boolean[][] figure = new boolean[image.getHeight()][image.getWidth()];
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                figure[y][x] = image.getRGB(x, y) == Color.BLACK.getRGB();
            }
        }
        gamePanel.setFigure(figure);
    }
}
