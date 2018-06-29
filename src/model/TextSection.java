package model;

import java.util.Objects;

public class TextSection extends Section {
    private String content;

    public TextSection(String content) {
        Objects.requireNonNull(content, "content can't be null");
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "TextSection{" +
                "content='" + content + '\'' +
                '}';
    }
}
