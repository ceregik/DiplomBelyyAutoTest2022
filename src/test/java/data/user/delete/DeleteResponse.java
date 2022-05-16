package data.user.delete;

public class DeleteResponse {

    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "DeleteResponse{" +
                "text='" + text + '\'' +
                '}';
    }
}