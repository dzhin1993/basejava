package model;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ListSection extends Section {
    public static final ListSection EMPTY = new ListSection(Collections.singletonList(""));
    private List<String> contents;

    public ListSection() {
    }

    public ListSection(List<String> contents) {
        Objects.requireNonNull(contents, "contents must not be null");
        this.contents = contents;
    }

    public List<String> getContents() {
        return contents;
    }

    public void setContents(List<String> contents) {
        this.contents = contents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListSection that = (ListSection) o;
        return Objects.equals(contents, that.contents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contents);
    }

    @Override
    public String toString() {
        return "ListSection{" +
                "contents=" + contents +
                '}';
    }
}
