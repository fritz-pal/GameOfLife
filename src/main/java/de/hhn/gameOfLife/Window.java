package de.hhn.gameOfLife;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Klasse der DesktopPane, die alle internal Frames beinhaltet.
 * Hier kann der Modus gesetzt werden und die Einstellungen der Fenster geändert werden.
 *
 * @author Felix Marzioch, Henri Staundenrausch
 * */

public class Window extends JFrame {
    private final JMenuItem run = new JMenuItem("Run");
    private final JMenuItem paint = new JMenuItem("Paint");
    private final JMenuItem figure = new JMenuItem("Figure");
    private final JMenuItem place = new JMenuItem("Place");
    private final JDesktopPane desktopPane = new JDesktopPane();
    private Mode mode = Mode.RUN;
    private Color deadColor = Color.WHITE;
    private Color aliveColor = Color.BLACK;
    private int columns = 100;
    private int rows = 100;
    private int zoom = 8;
    private boolean[][] figureArray = new boolean[0][0];

    public Window() {
        super("Game of Life");
        this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setLocationRelativeTo(null);
        this.setIconImage(Main.getIcon("figure.png").getImage());

        JMenu[] menus = {new JMenu("File"), new JMenu("Mode"), new JMenu("View"), new JMenu("Choose Figure")};

        run.addActionListener(e -> {
            mode = Mode.RUN;
            run.setIcon(Main.getIcon("selected.png"));
            paint.setIcon(null);
            place.setIcon(null);
            figure.setIcon(null);
        });
        paint.addActionListener(e -> {
            mode = Mode.PAINT;
            paint.setIcon(Main.getIcon("selected.png"));
            run.setIcon(null);
            place.setIcon(null);
            figure.setIcon(null);
        });
        place.addActionListener(e -> {
            mode = Mode.PLACE;
            place.setIcon(Main.getIcon("selected.png"));
            paint.setIcon(null);
            run.setIcon(null);
            figure.setIcon(null);
        });
        figure.addActionListener(e -> {
            mode = Mode.FIGURE;
            figure.setIcon(Main.getIcon("selected.png"));
            paint.setIcon(null);
            place.setIcon(null);
            run.setIcon(null);
        });
        run.setIcon(Main.getIcon("selected.png"));
        menus[1].add(run);
        menus[1].add(paint);
        menus[1].add(place);
        menus[1].add(figure);

        JMenuItem zoomItem = new JMenuItem("Zoom: " + zoom);
        zoomItem.setIcon(Main.getIcon("zoom.png"));
        zoomItem.addActionListener(e -> {
            JTextField textField = new JTextField();
            Object[] message = {"Zoom:", textField};
            int option = JOptionPane.showConfirmDialog(this, message, "Enter a zoom factor", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String text = textField.getText();
                try {
                    zoom = Integer.parseInt(text);
                    zoomItem.setText("Zoom: " + zoom);
                } catch (NumberFormatException ignored) {
                }
            }
        });
        menus[2].add(zoomItem);

        JMenuItem sizeItem = new JMenuItem("Size: " + columns + " x " + rows);
        sizeItem.setIcon(Main.getIcon("size.png"));
        sizeItem.addActionListener(e -> {
            JTextField textField1 = new JTextField();
            JTextField textField2 = new JTextField();
            Object[] message = {"Width:", textField1, "Height:", textField2};
            int option = JOptionPane.showConfirmDialog(this, message, "Enter the desired dimensions", JOptionPane.OK_CANCEL_OPTION);
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

        JMenuItem colorItem = new JMenuItem("Choose Color");
        colorItem.setIcon(Main.getIcon("color.png"));
        colorItem.addActionListener(e -> {
            JPanel deadButton = new JPanel();
            deadButton.setBackground(deadColor);
            deadButton.setPreferredSize(new Dimension(20, 20));
            deadButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            deadButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    Color color = JColorChooser.showDialog(null, "Choose a color", aliveColor);
                    if (color != null) {
                        deadButton.setBackground(color);
                    }
                }
            });
            JPanel aliveButton = new JPanel();
            aliveButton.setPreferredSize(new Dimension(20, 20));
            aliveButton.setBackground(aliveColor);
            aliveButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            aliveButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    Color color = JColorChooser.showDialog(null, "Choose a color", aliveColor);
                    if (color != null) {
                        aliveButton.setBackground(color);
                    }
                }
            });

            Object[] message = {"Ded Color:", deadButton, "Alive Color:", aliveButton};
            int option = JOptionPane.showConfirmDialog(this, message, "Choose the desired colors", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                deadColor = deadButton.getBackground();
                aliveColor = aliveButton.getBackground();
            }
        });
        menus[2].add(colorItem);

        JMenuItem close = new JMenuItem("Close");
        close.addActionListener(e -> System.exit(0));
        close.setIcon(Main.getIcon("exit.png"));

        JMenuItem createWindow = new JMenuItem("New Window");
        createWindow.setIcon(Main.getIcon("create.png"));
        createWindow.addActionListener(e -> {
            GOLWindow frame = new GOLWindow(this, columns, rows, zoom, deadColor, aliveColor);
            desktopPane.add(frame, 1);
        });
        menus[0].add(createWindow);
        menus[0].addSeparator();
        menus[0].add(close);
        JMenuBar menu = new JMenuBar();
        for (JMenu m : menus) menu.add(m);
        this.setJMenuBar(menu);

        String figuresPath = "src/main/resources/figures/";
        File[] files = new File(figuresPath).listFiles();
        if (files == null) throw new RuntimeException("No files found in " + figuresPath);
        for (File f : files) {
            if (!f.isDirectory()) continue;
            JMenu subMenu = new JMenu(getFormattedName(f.getName()));
            File[] subFiles = f.listFiles();
            if (subFiles != null) {
                for (File sf : subFiles) {
                    JMenuItem subMenuItem = new JMenuItem(getFormattedName(sf.getName()));
                    subMenuItem.setIcon(getFormattedIcon(sf.getPath()));
                    subMenuItem.addActionListener(e -> importFigure(sf.getPath()));
                    subMenu.add(subMenuItem);
                }
                if (subFiles.length >= 1) subMenu.setIcon(getFormattedIcon(subFiles[0].getPath()));
            }
            menus[3].add(subMenu);
        }

        desktopPane.setBackground(Color.LIGHT_GRAY);
        JLabel label = new JLabel();
        label.setSize(this.getWidth(), this.getHeight());
        this.add(desktopPane, 0);

        this.setVisible(true);
    }

    private String getFormattedName(String name) {
        String[] split = name.split("\\.");
        return Character.toUpperCase(split[0].charAt(0)) + split[0].substring(1).replace('_', ' ');
    }

    private ImageIcon getFormattedIcon(String path) {
        Image image = new ImageIcon(path).getImage();
        int i = image.getWidth(null) * 40 / image.getHeight(null);
        return new ImageIcon(image.getScaledInstance(i, 40, Image.SCALE_SMOOTH));
    }

    public void importFigure(String path) {
        File file = new File(path);
        BufferedImage image;
        try {
            image = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        boolean[][] figure = new boolean[image.getHeight()][image.getWidth()];
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                figure[y][x] = image.getRGB(x, y) == Color.BLACK.getRGB();
            }
        }
        figureArray = figure;
    }

    public Mode getMode() {
        return mode;
    }

    public boolean[][] getFigure() {
        return figureArray;
    }
}
