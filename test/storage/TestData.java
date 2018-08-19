package storage;

import model.*;

import java.time.Month;
import java.util.Arrays;
import java.util.UUID;

public class TestData {
    static final String UUID_1 = UUID.randomUUID().toString();
    static final String UUID_2 = UUID.randomUUID().toString();
    private static final String UUID_3 = UUID.randomUUID().toString();
    static final String UUID_4 = UUID.randomUUID().toString();
    public static final Resume R1 = new Resume(UUID_1, "Аня");
    static final Resume R2 = new Resume(UUID_2, "Аня");
    static final Resume R3 = new Resume(UUID_3, "Петя");
    static final Resume R4 = new Resume(UUID_4, "Коля");

    static {
        R1.setContact(ContactType.PHONE, "1");
        R2.setContact(ContactType.PHONE, "2");
        R3.setContact(ContactType.PHONE, "3");
        R4.setContact(ContactType.PHONE, "4");
        R1.setContact(ContactType.MAIL, "1@mail.ru");
        R2.setContact(ContactType.MAIL, "2@mail.ru");
        R3.setContact(ContactType.MAIL, "3@mail.ru");
        R4.setContact(ContactType.MAIL, "4@mail.ru");
        R1.setSection(SectionType.PERSONAL, new TextSection("1"));
        R2.setSection(SectionType.PERSONAL, new TextSection("2"));
        R3.setSection(SectionType.PERSONAL, new TextSection("3"));
        R4.setSection(SectionType.PERSONAL, new TextSection("4"));
        R1.setSection(SectionType.ACHIEVEMENT, new ListSection(Arrays.asList("1", "2")));
        R2.setSection(SectionType.ACHIEVEMENT, new ListSection(Arrays.asList("1", "2", "3")));
        R3.setSection(SectionType.ACHIEVEMENT, new ListSection(Arrays.asList("1", "2", "3", "4")));
        R4.setSection(SectionType.ACHIEVEMENT, new ListSection(Arrays.asList("1", "2", "3", "4", "5")));
        Company.Post post = new Company.Post("developer", 2016, Month.APRIL, 2017, Month.DECEMBER, null);
        Company.Post post2 = new Company.Post("developer2", 2012, Month.MARCH, 2018, Month.DECEMBER, null);
        Company company1 = new Company("good company", "company.com", post, post2);
        Company company2 = new Company("good company2", "company2.com", post, post2);
        R1.setSection(SectionType.EXPERIENCE, new CompanySection(Arrays.asList(company1, company2)));
        R2.setSection(SectionType.EXPERIENCE, new CompanySection(Arrays.asList(company1, company2)));
        R3.setSection(SectionType.EXPERIENCE, new CompanySection(Arrays.asList(company1, company2)));
        R4.setSection(SectionType.EXPERIENCE, new CompanySection(Arrays.asList(company1, company2)));
    }
}
