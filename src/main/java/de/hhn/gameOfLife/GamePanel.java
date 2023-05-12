package de.hhn.gameOfLife;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    static int ROWS = 100;
    static int COLUMNS = 100;
    private final Cell[][] cells = new Cell[ROWS][COLUMNS];

    private final boolean[][] lastStep = new boolean[ROWS][COLUMNS];
    private boolean mousePressed = false;
    private boolean figureMode = true;
    private boolean[][] figure = new boolean[0][0];

    public GamePanel() {
        this.setBounds(0, 0, COLUMNS * 8, ROWS * 8);
        this.setPreferredSize(new Dimension(800, 800));
        this.setLayout(new GridLayout(ROWS, COLUMNS));

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                cells[i][j] = new Cell(this, i, j);
            }
        }
    }

    public void update() {
        saveLastStep();

        int[][] allNeighbours = new int[ROWS][ROWS];

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                Cell cell = cells[i][j];
                int neighbours = 0;
                for (int k = -1; k <= 1; k++) {
                    for (int l = -1; l <= 1; l++) {
                        int x = (i + k) % ROWS;
                        if (x < 0) x += ROWS;
                        int y = (j + l) % COLUMNS;
                        if (y < 0) y += COLUMNS;

                        if (cells[x][y].isAlive()) neighbours++;
                    }
                }
                if (cell.isAlive()) neighbours--;
                allNeighbours[i][j] = neighbours;
            }
        }

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                cells[i][j].update(allNeighbours[i][j]);
            }
        }
    }

    private void saveLastStep() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                lastStep[i][j] = cells[i][j].isAlive();
            }
        }
    }

    public void loadLastStep() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                cells[i][j].setAlive(lastStep[i][j]);
            }
        }
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

    public void clear() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                cells[i][j].setAlive(false);
            }
        }
    }

    public void randomize() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                cells[i][j].setAlive(Math.random() > 0.5);
            }
        }
    }

    public void placeFigure(int x, int y) {
        for (int i = 0; i < figure.length; i++) {
            for (int j = 0; j < figure[i].length; j++) {
                cells[(x + i) % ROWS][(y + j) % COLUMNS].setAlive(figure[i][j]);
            }
        }
    }

    public void setFigure(boolean[][] figure) {
        this.figure = figure;
    }

    public boolean isFigureMode() {
        return figureMode;
    }
}
