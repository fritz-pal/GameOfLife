package de.hhn.gameOfLife;

import javax.swing.*;
import java.awt.*;

/**
 * Erstellen Sie das GUI-Programm GameOfLife als
 * eine spezielle Implementierung des
 * Spiels „Game of Life“ von Conway sein.
 *
 * @author Dennis Mayer, Lukas Vier, Felix Marzioch, Henri Staudenrausch
 * */

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }

        new Window();
    }

    public static ImageIcon getIcon(String path) {
        ImageIcon icon = new ImageIcon("src/main/resources/icons/" + path);
        return new ImageIcon(icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
    }
}
