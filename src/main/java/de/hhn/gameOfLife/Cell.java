package de.hhn.gameOfLife;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Cell extends JPanel {
    private boolean alive;
    GamePanel gamePanel;

    public Cell(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.alive = false;
        setColor();
        this.addMouseListener(mouseListener());
        this.gamePanel.add(this);
    }

    public boolean isAlive() {
        return alive;
    }

    public void update(int neighbours) {
        boolean lastState = alive;
        if (!alive) alive = neighbours == 3;
        else alive = !(neighbours < 2 || neighbours > 3);
        if (lastState != alive) setColor();
    }

    private void setColor() {
//        this.setBackground(alive ? new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)) : Color.WHITE);
//        this.setBackground(alive ? Color.BLACK : Color.WHITE);
        this.setBackground(alive ? Color.GREEN : Color.BLUE);
    }

    public void setAlive(boolean alive) {
        if(this.alive == alive) return;
        this.alive = alive;
        setColor();
    }

    private MouseAdapter mouseListener() {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setAlive(!alive);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                gamePanel.setMousePressed(true);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                gamePanel.setMousePressed(false);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (gamePanel.isMousePressed()) {
                    setAlive(true);
                }
            }
        };
    }
}
