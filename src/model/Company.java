package model;

import java.time.LocalDate;
import java.util.Objects;

public class Company {
    private final String position;
    private String description;
    private final LocalDate startWork;
    private final LocalDate endWork;
    private Link link;

    public Company(String name, String url, LocalDate startWork, LocalDate endWork, String position, String description) {
        Objects.requireNonNull(position, "position can't be null");
        Objects.requireNonNull(startWork, "startWork can't be null");
        Objects.requireNonNull(endWork, "endWork can't be null");
        this.link = new Link(name, url);
        this.startWork = startWork;
        this.endWork = endWork;
        this.position = position;
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Company company = (Company) o;
        return Objects.equals(position, company.position) &&
                Objects.equals(description, company.description) &&
                Objects.equals(startWork, company.startWork) &&
                Objects.equals(endWork, company.endWork) &&
                Objects.equals(link, company.link);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, description, startWork, endWork, link);
    }

    @Override
    public String toString() {
        return "Company{" +
                "position='" + position + '\'' +
                ", description='" + description + '\'' +
                ", startWork=" + startWork +
                ", endWork=" + endWork +
                ", link=" + link +
                '}';
    }
}
