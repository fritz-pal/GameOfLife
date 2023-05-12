package de.hhn.gameOfLife;

import javax.swing.*;
import javax.swing.plaf.MenuItemUI;
import javax.swing.plaf.basic.BasicMenuItemUI;
import javax.swing.plaf.synth.SynthMenuItemUI;
import java.awt.*;

public class Window extends JFrame {
    private final JMenuItem run = new JMenuItem("Run");
    private final JMenuItem paint = new JMenuItem("Paint");
    private final JMenuItem figure = new JMenuItem("Figure");
    private final JMenuItem place = new JMenuItem("Place");
    JMenuBar menu = new JMenuBar();
    JDesktopPane desktopPane = new JDesktopPane();
    private Mode mode = Mode.RUN;


    public Window() {
        super("Game of Life");
        this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setLocationRelativeTo(null);
        this.setIconImage(new ImageIcon("src/main/resources/spaceships/super_heavy_weight_spaceship.png").getImage());

        JMenu[] menus = {new JMenu("File"), new JMenu("Mode"), new JMenu("View"), new JMenu("Help")};

        run.addActionListener(e -> {
            mode = Mode.RUN;
            run.setBackground(Color.GREEN);
            paint.setBackground(null);
            place.setBackground(null);
            figure.setBackground(null);
        });
        run.setBackground(Color.GREEN);
        paint.addActionListener(e -> {
            mode = Mode.PAINT;
            run.setBackground(null);
            paint.setBackground(Color.GREEN);
            place.setBackground(null);
            figure.setBackground(null);
        });
        paint.setBackground(null);
        place.addActionListener(e -> {
            mode = Mode.PLACE;
            run.setBackground(null);
            paint.setBackground(null);
            place.setBackground(Color.GREEN);
            figure.setBackground(null);
        });
        place.setBackground(null);
        figure.addActionListener(e -> {
            mode = Mode.FIGURE;
            run.setBackground(null);
            paint.setBackground(null);
            place.setBackground(null);
            figure.setBackground(Color.GREEN);
        });
        menus[1].add(run);
        menus[1].add(paint);
        menus[1].add(place);
        menus[1].add(figure);

        JMenuItem close = new JMenuItem("Exit");
        close.addActionListener(e -> System.exit(0));

        JMenuItem createWindow = new JMenuItem("New Window");
        createWindow.addActionListener(e -> {
            GOLWindow frame = new GOLWindow(this, 50, 50);
            desktopPane.add(frame);
        });
        menus[0].add(createWindow);
        menus[0].add(close);
        for (JMenu m : menus) menu.add(m);
        this.setJMenuBar(menu);

        desktopPane.setBackground(Color.BLUE);
        this.add(desktopPane);

        this.setVisible(true);
    }

    public Mode getMode() {
        return mode;
    }
}
