package object;

public class Word {
    public static final int FONT_SIZE = 9;
    private int id;
    private String word;
    private String content;

    public Word(int id, String word, String content) {
        this.id = id;
        this.word = word;
        this.content = content;
    }

    public String getWord() {
        return word;
    }

    public String getContent() {
        return content;
    }

    public int getId() {
        return id;
    }

    public String getInformation() {
        return "<strong><font size = " + FONT_SIZE + ">" + word + "</font></strong><br><br>" + content;
    }

    @Override
    public String toString() {
        return word;
    }
}
