package de.hhn.gameOfLife;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {
    JMenuBar menu = new JMenuBar();
    JDesktopPane desktopPane = new JDesktopPane();

    public Window() {
        super("Game of Life");
        this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setLocationRelativeTo(null);
        this.setIconImage(new ImageIcon("src/main/resources/glider.png").getImage());
        JMenu[] menus = {new JMenu("File"), new JMenu("Mode"), new JMenu("View"), new JMenu("Help")};


        JMenuItem createWindow = new JMenuItem("New Window");
        createWindow.addActionListener(e -> {
            GOLWindow frame = new GOLWindow(100, 100);
            desktopPane.add(frame);
        });
        menus[0].add(createWindow);
        for (JMenu m : menus) menu.add(m);
        this.setJMenuBar(menu);

        desktopPane.setBackground(Color.BLUE);
        this.add(desktopPane);

        this.setVisible(true);
    }
}
