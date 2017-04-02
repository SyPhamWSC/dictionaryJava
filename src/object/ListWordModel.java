package object;

import database.DatabaseManager;
import gui.Container;

import javax.swing.*;
import java.util.List;
import java.util.Locale;

public class ListWordModel extends DefaultListModel<Word> {
    public static final int WORD_LOADED_PER_TURN = 200;
    public static final int WORD_COUNT = 400;
    public static final int WORD_HINT_COUNT = 50;
    private List<Word> listTemp;
    private boolean canLoadMoreWork;

    public ListWordModel() {
        initListWords();
        canLoadMoreWork = true;
    }

    public void initListWords() {
        listTemp = DatabaseManager.getInstance().getAllWordFromDatabase(getFirstId(), getFirstId() + WORD_COUNT);
        for (Word aWord : listTemp) {
            addElement(aWord);
        }
    }

    public void loadWordUp(int orient) {
        int topId = getFirstId();
        int lastId = getLastId();
        switch (orient) {
            case Container.SCROLL_BAR_AT_BOTTOM: {
                for (int i = 0; i < WORD_LOADED_PER_TURN; i++) {
                    remove(i);
                }
                int startIndex = getLastId();
                int endIndex = getLastId() + WORD_LOADED_PER_TURN;
                listTemp = DatabaseManager.getInstance().getAllWordFromDatabase(getLastId(), getLastId() + WORD_LOADED_PER_TURN);
                for (Word aWord : listTemp) {
                    addElement(aWord);
                }
            }
            break;

            case Container.SCROLL_BAR_AT_TOP: {
                for (int i = WORD_LOADED_PER_TURN - 1; i >= 0 ; i--) {
                    remove(0);
                }
//                listTemp = DatabaseManager.getInstance().getAllWordFromDatabase(getFirstId() - W);
                for (Word aWord : listTemp) {
                    add(0,aWord);
                }
            }
            break;

            default: {
                break;
            }
        }
    }

    public void rewindList() {
        clear();
        initListWords();
    }

    public void filter(String text) {
        clear();
        if (text.isEmpty()) {
            this.canLoadMoreWork = true;
            for (Word aWord : listTemp) {
                addElement(aWord);
            }
        } else {
            this.canLoadMoreWork = false;
            String lowerText = text.toLowerCase(Locale.getDefault());
            List<Word> listResult = DatabaseManager.getInstance().searchInDatabase(text);
            for (Word aWord : listResult) {
                if (aWord.getWord().toLowerCase(Locale.getDefault()).indexOf(lowerText) == 0) {
                    addElement(aWord);
                }
            }
        }
    }

    public boolean canLoadMoreWork() {
        return canLoadMoreWork;
    }

    public int getFirstId() {
        if (listTemp == null) {
            return 0;
        }
        return listTemp.get(0).getId();
    }

    public int getLastId() {
        if (listTemp == null) {
            return 0;
        }
        return listTemp.get(listTemp.size() - 1).getId();
    }
}
