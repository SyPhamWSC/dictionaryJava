package dialog;

import database.DatabaseManager;
import object.ListWordModel;
import object.Word;

import javax.swing.*;

public class AddWordDialog {
    public static final String LABEL_WORD_NAME = "Word  ";
    public static final String LABEL_CONTENT_NAME = "Content  ";

    private ListWordModel listWordModel;

    public AddWordDialog(ListWordModel listWordModel) {
        this.listWordModel = listWordModel;
        initDialog();
    }

    private void initDialog() {
        JLabel wordLabel = new JLabel(LABEL_WORD_NAME);
        JLabel contentLabel = new JLabel(LABEL_CONTENT_NAME);
        JTextField wordEdit = new JTextField();
        JTextArea contentEdit = new JTextArea();
        contentEdit.setRows(10);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(contentEdit);

        JComponent arrJComponents[] = new JComponent[]{wordLabel, wordEdit, contentLabel, scrollPane};

        int result = JOptionPane.showConfirmDialog(null,
                                                    arrJComponents,
                                                    "Add New Word",
                                                    JOptionPane.OK_CANCEL_OPTION);
        if(result == JOptionPane.OK_OPTION){
            Word inputWord = new Word(0, wordEdit.getText(), contentEdit.getText());
            if(DatabaseManager.getInstance().insertWordToDatabase(inputWord)){
                listWordModel.addElement(inputWord);
            }
        }
    }
}
