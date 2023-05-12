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
    //private final JLabel imageLabel = new JLabel();
//    private final JPanel taskBar = new JPanel();
//    private ImageIcon figureImage = null;

    public GOLWindow(Window window, int columns, int rows, int zoom) {
        super("Game of Life", false, true, false, true);
        this.window = window;
        this.setSize(columns * zoom, rows * zoom);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.getContentPane().setPreferredSize(new Dimension(columns * zoom, rows * zoom));
        this.setFrameIcon(new ImageIcon("src/main/resources/spaceships/super_heavy_weight_spaceship.png"));

        gamePanel = new GamePanel(this, columns, rows);
        this.add(gamePanel, BorderLayout.CENTER);

        gameThread = new GameThread(gamePanel, 500);
        gameThread.start();
        this.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosed(InternalFrameEvent e) {
                gameThread.dispose();
            }
        });
        JMenuBar menuGolWindow = new JMenuBar();
        JMenu[] golMenus = {new JMenu("Clear"), new JMenu("Set Speed"), new JMenu("Step"), new JMenu("Step back"), new JMenu("Randomize"), new JMenu("Choose Figure")};
        for (JMenu m : golMenus) menuGolWindow.add(m);
        this.setJMenuBar(menuGolWindow);

        golMenus[0].addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                gamePanel.clear();
            }
        });
        JSlider updateRateSlider = new JSlider(1, 1000, 500);
        updateRateSlider.setBounds(0, 800, 800, 50);
        updateRateSlider.addChangeListener(e -> gameThread.setUpdateRate(updateRateSlider.getValue()));
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


        String figuresPath = "src/main/resources/figures/";
        File[] files = new File(figuresPath).listFiles();
        if (files == null) throw new RuntimeException("No files found in " + figuresPath);
        for (File f : files) {
            if (!f.isDirectory()) continue;
            JMenu subMenu = new JMenu(getFormattedName(f.getName()));
            File[] subFiles = f.listFiles();
            if (subFiles != null) {
                for (File sf : subFiles) {
                    JMenuItem subMenuItem = new JMenuItem(getFormattedName(sf.getName()));
                    subMenuItem.setIcon(getFormattedIcon(sf.getPath()));
                    subMenuItem.addActionListener(e -> importFigure(sf.getPath()));
                    subMenu.add(subMenuItem);
                }
            }
            golMenus[5].add(subMenu);
        }

        // imageLabel.setBounds(800, 300, 200, 200);
        //imageLabel.setIcon(figureImage);
        // this.add(taskBar);
//
//        JButton importButton = new JButton("Import");
//        taskBar.add(importButton);
//        this.add(taskBar, BorderLayout.NORTH);

        this.setVisible(true);
        this.pack();
    }

    private String getFormattedName(String name) {
        String[] split = name.split("\\.");
        return Character.toUpperCase(split[0].charAt(0)) + split[0].substring(1).replace('_', ' ');
    }

    private ImageIcon getFormattedIcon(String path) {
        Image image = new ImageIcon(path).getImage();
        int i = image.getWidth(null) * 40 / image.getHeight(null);
        return new ImageIcon(image.getScaledInstance(i, 40, Image.SCALE_SMOOTH));
    }

    private void importFigure(String path) {
        File file = new File(path);
        BufferedImage image;
        try {
            image = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
//        figureImage = new ImageIcon(image.getScaledInstance(image.getWidth() * 4, image.getHeight() * 4, Image.SCALE_SMOOTH));
//        imageLabel.setIcon(figureImage);

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
