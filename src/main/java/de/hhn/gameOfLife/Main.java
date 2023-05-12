package de.hhn.gameOfLife;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
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

    public static Image getBackground() {
        String appData = System.getenv("APPDATA");
        String wallpaperPath = appData + "\\Microsoft\\Windows\\Themes\\TranscodedWallpaper";
        File wallpaperFile = new File(wallpaperPath);
        try {
            return ImageIO.read(wallpaperFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
