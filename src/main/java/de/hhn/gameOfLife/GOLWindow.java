package de.hhn.gameOfLife;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GOLWindow extends JInternalFrame {
    private final GamePanel gamePanel;
    private final GameThread gameThread;
    private final Window window;
    private final JLabel imageLabel = new JLabel();
    private final JPanel taskBar = new JPanel();
    private ImageIcon figureImage = null;

    public GOLWindow(Window window, int rows, int columns) {
        super("Game of Life", true, true, true, true);
        this.window = window;
        this.setSize(1000, 900);
        this.setLayout(new BorderLayout());
        this.setFrameIcon(new ImageIcon("src/main/resources/spaceships/super_heavy_weight_spaceship.png"));


        gamePanel = new GamePanel(this, rows, columns);
        this.add(gamePanel, BorderLayout.CENTER);

        gameThread = new GameThread(gamePanel, 990);
        gameThread.start();
        this.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosed(InternalFrameEvent e) {
                gameThread.dispose();
            }
        });
        JMenuBar menuGolWindow = new JMenuBar();
        JMenu[] golMenus = {new JMenu("Clear"), new JMenu("Set Speed"), new JMenu("Step"), new JMenu("Step back"), new JMenu("Randomize"), new JMenu("Insert Figure")};
        for (JMenu m : golMenus) menuGolWindow.add(m);
        this.setJMenuBar(menuGolWindow);

        golMenus[0].addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                gamePanel.clear();
            }
        });
        JSlider updateRateSlider = new JSlider(1, 1000, 800);
        updateRateSlider.setBounds(0, 800, 800, 50);
        updateRateSlider.addChangeListener(e -> {
            if (gameThread != null) {
                gameThread.setUpdateRate(updateRateSlider.getValue());
            }
        });



        golMenus[1].add(updateRateSlider);

        golMenus[2].addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                gamePanel.update();

            }
        });

        golMenus[3].addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                gamePanel.loadLastStep();

            }
        });

        golMenus[4].addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                gamePanel.randomize();

            }
        });


        JButton importButton = new JButton("Place Glider");
        importButton.setBounds(800, 250, 100, 50);
        importButton.addActionListener(e -> importFigure("oscillators/glider_gun.png"));


       // imageLabel.setBounds(800, 300, 200, 200);
       //imageLabel.setIcon(figureImage);
       // this.add(taskBar);

        importFigure("oscillators/pulsar.png");

        taskBar.add(importButton);
        this.add(taskBar, BorderLayout.NORTH);

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
        figureImage = new ImageIcon(image.getScaledInstance(image.getWidth() * 4, image.getHeight() * 4, Image.SCALE_SMOOTH));
        imageLabel.setIcon(figureImage);

        boolean[][] figure = new boolean[image.getHeight()][image.getWidth()];
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                figure[y][x] = image.getRGB(x, y) == Color.BLACK.getRGB();
            }
        }
        gamePanel.setFigure(figure);
    }

    public Mode getMode() {
        return window.getMode();
    }
}
