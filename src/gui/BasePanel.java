package gui;

import javax.swing.*;

public abstract class BasePanel extends JPanel {
    public BasePanel(){
        initPanel();
        initComps();
        addComps();
    }

    protected abstract void initPanel();

    protected abstract void initComps();

    protected abstract void addComps();
}
