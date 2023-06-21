package de.hhn.gameOfLife;

import javax.swing.*;
import java.awt.*;

/**
 * Panel für das Spielfeld. Erstellt die Zellen und verwaltet die Logik des Spiels.
 * Die Update-Methode lässt das Spiel eine Generation weiterlaufen.
 *
 * @author Henri Staudenrausch
 * */

public class GamePanel extends JPanel {
    private final Cell[][] cells;
    private final boolean[][] lastStep;
    private final GOLWindow window;
    private final int rows;
    private final int columns;
    private boolean mousePressed = false;
    private final Color deadColor;
    private final Color aliveColor;

    public GamePanel(GOLWindow window, int columns, int rows, Color deadColor, Color aliveColor) {
        this.window = window;
        this.columns = columns;
        this.rows = rows;
        this.deadColor = deadColor;
        this.aliveColor = aliveColor;
        this.setBounds(0, 0, columns * 8, rows * 8);
        this.setPreferredSize(new Dimension(800, 800));
        this.setLayout(new GridLayout(rows, columns));
        this.setFocusable(false);
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

        int[][] allNeighbours = new int[rows][columns];

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
                if(cells[i][j].isAlive()) cells[i][j].setAlive(false);
            }
        }
    }

    public void fill() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if(!cells[i][j].isAlive()) cells[i][j].setAlive(true);
            }
        }
    }

    public void randomize() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                boolean alive = Math.random() > 0.5;
                if(cells[i][j].isAlive() != alive) cells[i][j].setAlive(alive);
            }
        }
    }

    public void placeFigure(int xp, int yp) {
        boolean[][] figure = window.getFigure();
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

    public Mode getMode() {
        return window.getMode();
    }

    public Color getDeadColor() {
        return deadColor;
    }

    public Color getAliveColor() {
        return aliveColor;
    }
}
