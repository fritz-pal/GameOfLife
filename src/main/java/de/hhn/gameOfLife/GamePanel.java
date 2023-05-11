package de.hhn.gameOfLife;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {
    static int ROWSANDCOLUMNS = 100;
    private final Cell[][] cells = new Cell[ROWSANDCOLUMNS][ROWSANDCOLUMNS];

    public Window() {
        super("Game of Life");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1016, 1039);
        this.setLocationRelativeTo(null);
        this.setResizable(true);
        this.setLayout(new GridLayout(ROWSANDCOLUMNS, ROWSANDCOLUMNS));

        for (int i = 0; i < ROWSANDCOLUMNS; i++) {
            for (int j = 0; j < ROWSANDCOLUMNS; j++) {
                cells[i][j] = new Cell(this);
            }
        }


        this.setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        System.out.println("Width: " + this.getWidth() + " Height: " + this.getHeight());
    }

    public void update() {
        int[][] allNeighbours = new int[ROWSANDCOLUMNS][ROWSANDCOLUMNS];

        for (int i = 0; i < ROWSANDCOLUMNS; i++) {
            for (int j = 0; j < ROWSANDCOLUMNS; j++) {
                Cell cell = cells[i][j];
                int neighbours = 0;
                for (int k = -1; k <= 1; k++) {
                    for (int l = -1; l <= 1; l++) {
                        int x = (i + k) % ROWSANDCOLUMNS;
                        if (x < 0) x += ROWSANDCOLUMNS;
                        int y = (j + l) % ROWSANDCOLUMNS;
                        if (y < 0) y += ROWSANDCOLUMNS;

                        if (cells[x][y].isAlive()) neighbours++;
                    }
                }
                if (cell.isAlive()) neighbours--;
                allNeighbours[i][j] = neighbours;
            }
        }

        for (int i = 0; i < ROWSANDCOLUMNS; i++) {
            for (int j = 0; j < ROWSANDCOLUMNS; j++) {
                cells[i][j].update(allNeighbours[i][j]);
            }
        }
    }
}
