package panels.editor.textdialog;

public class TextProperties {
    private String text;
    private int fontSize;
    private String fontType;
    private String fontStyle;

    public TextProperties(String text, int fontSize, String fontType, String fontStyle) {
        this.text = text;
        this.fontSize = fontSize;
        this.fontType = fontType;
        this.fontStyle = fontStyle;
    }

    public String getText() {
        return text;
    }

    public int getFontSize() {
        return fontSize;
    }

    public String getFontType() {
        return fontType;
    }

    public String getFontStyle() {
        return fontStyle;
    }

    @Override
    public String toString() {
        return "Text: " + text + "\nFont Size: " + fontSize + "\nFont Type: " + fontType + "\nFont Style: " + fontStyle;
    }
}
