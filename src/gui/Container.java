package gui;

import database.DatabaseManager;
import dialog.AddWordDialog;
import object.Word;
import object.ListWordModel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;

public class Container extends BasePanel implements ActionListener, ListSelectionListener, AdjustmentListener {
    public static final int PANEL_WIDTH = 800;
    public static final int PANEL_HEIGHT = 600;

    public static final int PADDING = 5;

    public static final int SEARCH_BAR_WIDTH = 150;
    public static final int BUTTON_SEARCH_WIDTH = 70;

    public static final String BUTTON_SEARCH_LABEL = "Search";
    public static final String BUTTON_ADD_LABEL = "Add";
    public static final String BUTTON_REMOVE_LABEL = "Remove";

    public static final String BUTTON_SWITCH_LABEL_1 = "Vi - Eng";
    public static final String BUTTON_SWITCH_LABEL_2 = "Eng - Vi";

    public static final int SCROLL_BAR_AT_TOP = 0;
    public static final int SCROLL_BAR_AT_BOTTOM = 1;

    private static final String BUTTON_ADD_COMMAND = "0";
    private static final String BUTTON_REMOVE_COMMAND = "1";
    private static final String BUTTON_SWITCH_COMMAND = "2";
    private static final String BUTTON_SEARCH_COMMAND = "3";

    private static final int NO_ITEM_SELECTED = -1;

    private JList<Word> listWord;
    private JScrollPane scrollPaneListWord;
    private JScrollPane scrollPaneWordContent;
    private JTextField tfSearch;
    private JButton btnSearch;
    private JButton btnAdd;
    private JButton btnRemove;
    private JButton btnSwitchDatabaseToTranslate;
    private ListWordModel listWordModel;
    private JTextPane tpWordContent;
    private int selectedItemIndex;

    @Override
    protected void initPanel() {
        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(PANEL_WIDTH + 3 * PADDING, PANEL_HEIGHT + 3 * PADDING));
    }

    @Override
    protected void initComps() {
        selectedItemIndex = NO_ITEM_SELECTED;
        listWordModel = new ListWordModel();
        int heightFont = getFontMetrics(GUI.FONT).getHeight();

        tfSearch = new JTextField();
        tfSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                listWordModel.filter(tfSearch.getText());
            }
        });

        tfSearch.setPreferredSize(new Dimension(SEARCH_BAR_WIDTH, heightFont));

        btnSearch = new JButton(BUTTON_SEARCH_LABEL);
        btnSearch.addActionListener(this);
        btnSearch.setActionCommand(BUTTON_SEARCH_COMMAND);
        btnSearch.setPreferredSize(new Dimension(BUTTON_SEARCH_WIDTH, heightFont));

        listWord = new JList<>();
        listWord.setModel(listWordModel);
        listWord.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listWord.addListSelectionListener(this);
        listWord.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Word word = listWordModel.getElementAt(selectedItemIndex);
                    tpWordContent.setText(word.getInformation());
                }
            }
        });
        scrollPaneListWord = new JScrollPane();
        scrollPaneListWord.setViewportView(listWord);
        scrollPaneListWord.getVerticalScrollBar().addAdjustmentListener(this);
        scrollPaneListWord.setPreferredSize(new Dimension(SEARCH_BAR_WIDTH + BUTTON_SEARCH_WIDTH, PANEL_HEIGHT - heightFont));

        int buttonWidth = (PANEL_WIDTH - SEARCH_BAR_WIDTH - BUTTON_SEARCH_WIDTH) / 3;
        btnAdd = new JButton(BUTTON_ADD_LABEL);
        btnAdd.addActionListener(this);
        btnAdd.setActionCommand(BUTTON_ADD_COMMAND);
        btnAdd.setPreferredSize(new Dimension(buttonWidth, heightFont));

        btnRemove = new JButton(BUTTON_REMOVE_LABEL);
        btnRemove.addActionListener(this);
        btnRemove.setActionCommand(BUTTON_REMOVE_COMMAND);
        btnRemove.setPreferredSize(new Dimension(buttonWidth, heightFont));

        btnSwitchDatabaseToTranslate = new JButton(BUTTON_SWITCH_LABEL_1);
        btnSwitchDatabaseToTranslate.addActionListener(this);
        btnSwitchDatabaseToTranslate.setActionCommand(BUTTON_SWITCH_COMMAND);
        btnSwitchDatabaseToTranslate.setPreferredSize(new Dimension(buttonWidth, heightFont));

        tpWordContent = new JTextPane();
        tpWordContent.setContentType("text/html");
        scrollPaneWordContent = new JScrollPane();
        scrollPaneWordContent.setViewportView(tpWordContent);
        scrollPaneWordContent.setPreferredSize(new Dimension(buttonWidth * 3, PANEL_HEIGHT - heightFont));
    }

    @Override
    protected void addComps() {
        GridBagConstraints gc = new GridBagConstraints();
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.gridx = 0;
        gc.gridy = 0;
        gc.insets = new Insets(PADDING, PADDING, PADDING, 0);
        add(tfSearch, gc);

        gc.insets = new Insets(PADDING, 0, PADDING, 0);
        gc.gridx = 1;
        gc.gridy = 0;
        add(btnSearch, gc);

        gc.insets = new Insets(PADDING, PADDING, PADDING, 0);
        gc.gridx = 2;
        gc.gridy = 0;
        add(btnAdd, gc);
        gc.insets = new Insets(PADDING, 0, PADDING, 0);
        gc.gridx = 3;
        gc.gridy = 0;
        add(btnRemove, gc);
        gc.insets = new Insets(PADDING, 0, PADDING, PADDING);
        gc.gridx = 4;
        gc.gridy = 0;
        add(btnSwitchDatabaseToTranslate, gc);

        gc.insets = new Insets(0, PADDING, PADDING, 0);
        gc.gridx = 0;
        gc.gridy = 1;
        gc.gridwidth = 2;
        add(scrollPaneListWord, gc);

        gc.insets = new Insets(0, PADDING, PADDING, 5);
        gc.gridx = 2;
        gc.gridy = 1;
        gc.gridwidth = 3;
        add(scrollPaneWordContent, gc);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case BUTTON_ADD_COMMAND: {
                new AddWordDialog(listWordModel);
            }
            break;

            case BUTTON_REMOVE_COMMAND: {
                if (selectedItemIndex != NO_ITEM_SELECTED) {
                    if (DatabaseManager.getInstance()
                            .deleteWordInDatabase(listWordModel
                                    .getElementAt(selectedItemIndex).getId())) {
                        listWordModel.remove(selectedItemIndex);
                    }
                    selectedItemIndex = NO_ITEM_SELECTED;
                }
            }
            break;

            case BUTTON_SWITCH_COMMAND: {
                DatabaseManager.getInstance().swapMode();
                if (btnSwitchDatabaseToTranslate.getText().equals(BUTTON_SWITCH_LABEL_1)) {
                    listWordModel.rewindList();
                    btnSwitchDatabaseToTranslate.setText(BUTTON_SWITCH_LABEL_2);
                } else {
                    listWordModel.rewindList();
                    btnSwitchDatabaseToTranslate.setText(BUTTON_SWITCH_LABEL_1);
                }
                tfSearch.setText("");
            }
            break;

            case BUTTON_SEARCH_COMMAND: {
                listWord.setSelectedIndex(0);
                tpWordContent.setText(listWordModel.getElementAt(0).getInformation());
            }
            break;

            default: {
                break;
            }
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        ListSelectionModel listSelectionModel = ((JList) e.getSource()).getSelectionModel();
        if (!listSelectionModel.isSelectionEmpty()) {
            int minIndex = listSelectionModel.getMinSelectionIndex();
            int maxIndex = listSelectionModel.getMaxSelectionIndex();
            for (int i = minIndex; i <= maxIndex; i++) {
                if (listSelectionModel.isSelectedIndex(i)) {
                    selectedItemIndex = i;
                }
            }
        }

    }

    @Override
    public void adjustmentValueChanged(AdjustmentEvent e) {
        if (e.getValueIsAdjusting() || !listWordModel.canLoadMoreWork()) {
            return;
        }
        JScrollBar scrollBar = (JScrollBar) e.getAdjustable();
        int barLength = scrollBar.getModel().getExtent();
        if (e.getValue() == 0) {

        } else if (e.getValue() + barLength == scrollBar.getMaximum()) {
            listWordModel.initListWords();
        }
    }
}
