package de.hhn.gameOfLife;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GOLWindow extends JInternalFrame {
    private final GamePanel gamePanel;
    private final GameThread gameThread;
    private final Window window;

    public GOLWindow(Window window, int columns, int rows, int zoom, Color deadColor, Color aliveColor) {
        super("Game of Life", false, true, false, true);
        this.window = window;
        this.setSize(columns * zoom, rows * zoom);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.getContentPane().setPreferredSize(new Dimension(columns * zoom, rows * zoom));
        this.setFrameIcon(Main.getIcon("figure.png"));
        this.addKeyListener(keyListener());
        this.setFocusable(true);

        gamePanel = new GamePanel(this, columns, rows, deadColor, aliveColor);
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
        JMenu[] golMenus = {new JMenu("Modify"), new JMenu("Speed")};
        for (JMenu m : golMenus) menuGolWindow.add(m);
        this.setJMenuBar(menuGolWindow);

        JMenuItem[] modifyItems = {new JMenuItem("Clear"), new JMenuItem("Randomize"), new JMenuItem("Fill")};
        modifyItems[0].addActionListener(e -> gamePanel.clear());
        modifyItems[0].setIcon(Main.getIcon("clear.png"));
        modifyItems[1].addActionListener(e -> gamePanel.randomize());
        modifyItems[1].setIcon(Main.getIcon("random.png"));
        modifyItems[2].addActionListener(e -> gamePanel.fill());
        modifyItems[2].setIcon(Main.getIcon("fill.png"));
        for (JMenuItem m : modifyItems) golMenus[0].add(m);

        JSlider updateRateSlider = new JSlider(1, 1000, 500);
        updateRateSlider.setBounds(0, 800, 800, 50);
        updateRateSlider.addChangeListener(e -> gameThread.setUpdateRate(updateRateSlider.getValue()));
        golMenus[1].add(updateRateSlider);

        this.setVisible(true);
        this.pack();
    }

    private KeyListener keyListener() {
        return new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 39) {
                    gamePanel.update();
                } else if (e.getKeyCode() == 37) {
                    gamePanel.loadLastStep();
                }
            }
        };
    }

    public boolean[][] getFigure() {
        return window.getFigure();
    }

    public Mode getMode() {
        return window.getMode();
    }
}
