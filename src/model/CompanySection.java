package model;

import java.util.List;
import java.util.Objects;

public class CompanySection extends Section {
    private List<Company> companies;

    public CompanySection(List<Company> companies) {
        Objects.requireNonNull(companies, "companies can't be null");
        this.companies = companies;
    }

    public List<Company> getCompanyes() {
        return companies;
    }

    public void setCompanyes(List<Company> companies) {
        this.companies = companies;
    }

    @Override
    public String toString() {
        return "CompanySection{" +
                "companies=" + companies +
                '}';
    }
}
