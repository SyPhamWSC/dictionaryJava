package database;

import object.ListWordModel;
import object.Word;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String DRIVER = "org.sqlite.JDBC";
    private static final String DATABASE_ENG_VI_URL = "jdbc:sqlite:anh_viet";
    private static final String DATABASE_VI_ENG_URL = "jdbc:sqlite:viet_anh";

    private static final String ENG_VI_TABLE_NAME = "anh_viet";
    private static final String VI_ENG_TABLE_NAME = "viet_anh";
    private static final String ID_COLUMN_NAME = "id";
    private static final String WORD_COLUMN_NAME = "word";
    private static final String CONTENT_COLUMN_NAME = "content";

    private static DatabaseManager instance = new DatabaseManager();
    private Connection connection;
    private String currentURL;
    private String currentTable;


    public static DatabaseManager getInstance() {
        return instance;
    }

    private DatabaseManager() {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        this.currentURL = DATABASE_ENG_VI_URL;
        this.currentTable = ENG_VI_TABLE_NAME;
    }

    private void openDatabase() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(currentURL);
            connection.setAutoCommit(true);
        }
    }

    private void closeDatabase() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    public void swapMode() {
        if (this.currentURL.equals(DATABASE_ENG_VI_URL)) {
            this.currentURL = DATABASE_VI_ENG_URL;
            this.currentTable = VI_ENG_TABLE_NAME;
        } else {
            this.currentURL = DATABASE_ENG_VI_URL;
            this.currentTable = ENG_VI_TABLE_NAME;
        }
    }

    public List<Word> getAllWordFromDatabase(int startID, int endID) {
        try {
            openDatabase();
            List<Word> result = new ArrayList<>();
            PreparedStatement statement = connection
                    .prepareStatement("SELECT * FROM " + currentTable +
                                    " WHERE " + ID_COLUMN_NAME + " >= " + startID +
                                    " AND " + ID_COLUMN_NAME + " <= " + endID +
                                    " ORDER BY " + WORD_COLUMN_NAME + " ASC;");
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            while (!resultSet.isAfterLast()) {
                result.add(new Word(resultSet.getInt(ID_COLUMN_NAME),
                        resultSet.getString(WORD_COLUMN_NAME),
                        resultSet.getString(CONTENT_COLUMN_NAME)));
                resultSet.next();
            }

            resultSet.close();
            statement.close();
            closeDatabase();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean hasWord(String word) {
        boolean result = true;
        try {
            openDatabase();
            PreparedStatement statement = connection
                    .prepareStatement("SELECT * FROM " + currentTable +
                                        " WHERE " + WORD_COLUMN_NAME + " = '" + word + "'" +
                                        "ORDER BY 1;");
            ResultSet resultSet = statement.executeQuery();
            result = resultSet.next();
            resultSet.close();
            statement.close();
            closeDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<Word> searchInDatabase(String word) {
        List<Word> result = new ArrayList<>();
        try {
            openDatabase();
            PreparedStatement statement = connection
                    .prepareStatement("SELECT * FROM " + currentTable +
                                        " WHERE " + WORD_COLUMN_NAME + " LIKE '" + word + "%';");
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            int wordCount = ListWordModel.WORD_HINT_COUNT;
            int size = 0;
            while (!resultSet.isAfterLast()) {
                result.add(new Word(resultSet.getInt(ID_COLUMN_NAME),
                        resultSet.getString(WORD_COLUMN_NAME),
                        resultSet.getString(CONTENT_COLUMN_NAME)));
                if (size++ > wordCount) {
                    break;
                }
                resultSet.next();
            }
            resultSet.close();
            statement.close();
            closeDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean insertWordToDatabase(Word word) {
        if(hasWord(word.getWord())){
            int result = JOptionPane.showConfirmDialog(null,
                                                        "Your new word has existed! Do you want to replace it?",
                                                        "Update Word",
                                                        JOptionPane.OK_CANCEL_OPTION);
            if(result == JOptionPane.OK_OPTION){
                updateWordInDatabase(word);
            }
            return true;
        }
        try {
            openDatabase();
            PreparedStatement statement = connection
                    .prepareStatement("INSERT INTO " + currentTable +
                                        " (" + WORD_COLUMN_NAME + "," + CONTENT_COLUMN_NAME + ")" +
                                        " VALUES ('" + word.getWord() + "', '" + word.getContent() + "');");
            statement.executeUpdate();
            statement.close();
            closeDatabase();
            JOptionPane.showMessageDialog(null,
                                        "Your new word has been added!");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                                            "An error occurred",
                                            "Error",
                                            JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private void updateWordInDatabase(Word word){
        try {
            openDatabase();
            PreparedStatement statement = connection
                    .prepareStatement("UPDATE " + currentTable+
                                        " SET " + CONTENT_COLUMN_NAME + " = '" + word.getContent() + "'" +
                                        " WHERE " + WORD_COLUMN_NAME + " = '" + word.getWord() + "';");
            statement.executeUpdate();
            statement.close();
            closeDatabase();
            JOptionPane.showMessageDialog(null,
                                        "Your new word has been added!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                                        "An error occurred",
                                        "Error",
                                        JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean deleteWordInDatabase(int id) {
        try {
            openDatabase();
            PreparedStatement statement = connection
                    .prepareStatement("DELETE FROM " + currentTable +
                                        " WHERE " + ID_COLUMN_NAME + " = " + id + ";");
            statement.executeUpdate();
            statement.close();
            closeDatabase();
            JOptionPane.showMessageDialog(null,
                                        "Your Selected Word Has Been Removed!",
                                        "Message",
                                        JOptionPane.INFORMATION_MESSAGE);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                                        "An Error Occurred",
                                        "Error",
                                        JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}
