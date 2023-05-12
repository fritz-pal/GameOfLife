package de.hhn.gameOfLife;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private final Cell[][] cells;
    private final boolean[][] lastStep;
    private final GOLWindow window;
    private final int rows;
    private final int columns;
    private boolean mousePressed = false;
    private boolean[][] figure = new boolean[0][0];


    public GamePanel(GOLWindow window, int columns, int rows) {
        this.window = window;
        this.columns = columns;
        this.rows = rows;
        this.setBounds(0, 0, columns * 8, rows * 8);
        this.setPreferredSize(new Dimension(800, 800));
        this.setLayout(new GridLayout(rows, columns));
        cells = new Cell[rows][columns];
        lastStep = new boolean[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                cells[i][j] = new Cell(this, i, j);
            }
        }
    }

    public void update() {
        saveLastStep();

        int[][] allNeighbours = new int[rows][rows];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                Cell cell = cells[i][j];
                int neighbours = 0;
                for (int k = -1; k <= 1; k++) {
                    for (int l = -1; l <= 1; l++) {
                        int x = (i + k) % rows;
                        if (x < 0) x += rows;
                        int y = (j + l) % columns;
                        if (y < 0) y += columns;

                        if (cells[x][y].isAlive()) neighbours++;
                    }
                }
                if (cell.isAlive()) neighbours--;
                allNeighbours[i][j] = neighbours;
            }
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                cells[i][j].update(allNeighbours[i][j]);
            }
        }
    }

    private void saveLastStep() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                lastStep[i][j] = cells[i][j].isAlive();
            }
        }
    }

    public void loadLastStep() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
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
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                cells[i][j].setAlive(false);
            }
        }
    }

    public void randomize() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                cells[i][j].setAlive(Math.random() > 0.5);
            }
        }
    }

    public void placeFigure(int xp, int yp) {
        if (figure.length == 0) return;
        int xStart = xp - figure.length / 2;
        int yStart = yp - figure[0].length / 2;
        for (int i = 0; i < figure.length; i++) {
            for (int j = 0; j < figure[i].length; j++) {
                int x = (xStart + i) % rows;
                if (x < 0) x += rows;
                int y = (yStart + j) % columns;
                if (y < 0) y += columns;
                cells[x][y].setAlive(figure[i][j]);
            }
        }
    }

    public void setFigure(boolean[][] figure) {
        this.figure = figure;
    }

    public Mode getMode() {
        return window.getMode();
    }
}
