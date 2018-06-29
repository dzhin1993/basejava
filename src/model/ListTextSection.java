package model;

import java.util.List;
import java.util.Objects;

public class ListTextSection extends Section{
    private List<String> contents;

    public ListTextSection(List<String> contents) {
        Objects.requireNonNull(contents, "contents can't be null");
        this.contents = contents;
    }

    public List<String> getContents() {
        return contents;
    }

    public void setContents(List<String> contents) {
        this.contents = contents;
    }

    @Override
    public String toString() {
        return "ListTextSection{" +
                "contents=" + contents +
                '}';
    }
}
