package model;

import java.net.URL;
import java.time.LocalDate;
import java.util.Objects;

public class Company {
    private String name;
    private String position;
    private String description;
    private LocalDate startWork;
    private LocalDate endWork;
    private URL url;

    public Company(String name, String position, LocalDate startWork, LocalDate endWork) {
        Objects.requireNonNull(name, "name can't be null");
        Objects.requireNonNull(position, "position can't be null");
        Objects.requireNonNull(startWork, "startWork can't be null");
        Objects.requireNonNull(endWork, "endWork can't be null");
        this.name = name;
        this.position = position;
        this.startWork = startWork;
        this.endWork = endWork;
    }

    public String getName() {
        return name;
    }

    public String getPosition() {
        return position;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getStartWork() {
        return startWork;
    }

    public LocalDate getEndWork() {
        return endWork;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Company company = (Company) o;
        return Objects.equals(name, company.name) &&
                Objects.equals(position, company.position) &&
                Objects.equals(description, company.description) &&
                Objects.equals(startWork, company.startWork) &&
                Objects.equals(endWork, company.endWork) &&
                Objects.equals(url, company.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, position, description, startWork, endWork, url);
    }

    @Override
    public String toString() {
        return "Company{" +
                "name='" + name + '\'' +
                ", position='" + position + '\'' +
                ", description='" + description + '\'' +
                ", startWork=" + startWork +
                ", endWork=" + endWork +
                ", url=" + url +
                '}';
    }
}
