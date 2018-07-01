package model;

import java.util.List;
import java.util.Objects;

public class ListSection extends Section {
    private final List<String> contents;

    public ListSection(List<String> contents) {
        Objects.requireNonNull(contents, "contents can't be null");
        this.contents = contents;
    }

    public List<String> getContents() {
        return contents;
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
