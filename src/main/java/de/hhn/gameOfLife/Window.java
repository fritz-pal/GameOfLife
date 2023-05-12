package de.hhn.gameOfLife;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {
    private final JMenuItem run = new JMenuItem("Run");
    private final JMenuItem paint = new JMenuItem("Paint");
    private final JMenuItem figure = new JMenuItem("Figure");
    private final JMenuItem place = new JMenuItem("Place");
    JMenuBar menu = new JMenuBar();
    JDesktopPane desktopPane = new JDesktopPane();
    private Mode mode = Mode.RUN;
    private int columns = 100;
    private int rows = 100;
    private int zoom = 8;

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

        JMenuItem zoomItem = new JMenuItem("Zoom: " + zoom);
        zoomItem.addActionListener(e -> {
            JTextField textField = new JTextField();
            Object[] message = {"Zoom:", textField};
            int option = JOptionPane.showConfirmDialog(null, message, "Enter a zoom factor", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String text = textField.getText();
                try {
                    int number = Integer.parseInt(text);
//                    if (number >= 1 && number <= 20) zoom = number;
                    zoom = number;
                    zoomItem.setText("Zoom: " + zoom);
                } catch (NumberFormatException ignored) {
                }
            }
        });
        menus[2].add(zoomItem);
        JMenuItem sizeItem = new JMenuItem("Size: " + columns + " x " + rows);
        sizeItem.addActionListener(e -> {
            JTextField textField1 = new JTextField();
            JTextField textField2 = new JTextField();
            Object[] message = {"Width:", textField1, "Height:", textField2};
            int option = JOptionPane.showConfirmDialog(null, message, "Enter the desired dimensions", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String text1 = textField1.getText();
                String text2 = textField2.getText();
                try {
                    int number1 = Integer.parseInt(text1);
                    int number2 = Integer.parseInt(text2);
                    columns = number1;
                    rows = number2;
                    sizeItem.setText("Size: " + columns + " x " + rows);
                } catch (NumberFormatException ignored) {
                }
            }
        });
        menus[2].add(sizeItem);


        JMenuItem close = new JMenuItem("Exit");
        close.addActionListener(e -> System.exit(0));

        JMenuItem createWindow = new JMenuItem("New Window");
        createWindow.addActionListener(e -> {
            GOLWindow frame = new GOLWindow(this, columns, rows, zoom);
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
