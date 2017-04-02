package gui;

import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame {
    public static Font FONT = new Font("tahoma", Font.PLAIN, 18);

    private Container container;

    public GUI() {
        initGUI();
        initComps();
        addComps();
    }

    private void initGUI() {
        setSize(Container.PANEL_WIDTH, Container.PANEL_HEIGHT);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void initComps() {
        container = new Container();
    }

    private void addComps() {
        add(container);
        pack();
    }

}
