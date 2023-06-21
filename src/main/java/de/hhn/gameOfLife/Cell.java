package de.hhn.gameOfLife;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Hilfsklasse für die Zellen des Spielfelds. Prüft auf Mausklicks und ändert die Farbe der Zelle.
 *
 * @author Dennis Mayer
 * */

public class Cell extends JPanel {
    private final GamePanel gamePanel;
    private final int x, y;
    private boolean alive;

    public Cell(GamePanel gamePanel, int x, int y) {
        this.x = x;
        this.y = y;
        this.gamePanel = gamePanel;
        this.alive = false;
        setColor();
        this.setFocusable(false);
        this.addMouseListener(mouseListener());
        this.gamePanel.add(this);
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        if (this.alive == alive) return;
        this.alive = alive;
        setColor();
    }

    public void update(int neighbours) {
        boolean lastState = alive;
        if (!alive) alive = neighbours == 3;
        else alive = !(neighbours < 2 || neighbours > 3);
        if (lastState != alive) setColor();
    }

    private void setColor() {
        this.setBackground(alive ? gamePanel.getAliveColor() : gamePanel.getDeadColor());
    }

    private MouseAdapter mouseListener() {
        return new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                gamePanel.setMousePressed(true);
                switch (gamePanel.getMode()) {
                    case PLACE -> setAlive(!alive);
                    case PAINT -> setAlive(true);
                    case FIGURE -> gamePanel.placeFigure(x, y);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                gamePanel.setMousePressed(false);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (gamePanel.isMousePressed()) {
                    if (gamePanel.getMode() == Mode.PAINT) setAlive(true);
                }
            }
        };
    }
}
