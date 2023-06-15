package de.hhn.gameOfLife;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

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
        return new ImageIcon(icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH));
    }
}
