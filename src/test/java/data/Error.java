package data;

public class Error {
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Error{" +
                "text='" + text + '\'' +
                '}';
    }
}
