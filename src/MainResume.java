import model.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;

public class MainResume {
    public static void main(String[] args) {
        TextSection section = new TextSection("java junior");
        TextSection section1 = new TextSection("good");
        ListTextSection section2 = new ListTextSection(Arrays.asList("bla","bla","bla"));
        LocalDate localDate = LocalDate.now();
        LocalDate localDate1 = LocalDate.now();
        Company company = new Company("good company", "developer",localDate,localDate1);
        CompanySection companySection = new CompanySection(Collections.singletonList(company));
        Resume resume = new Resume("Ilya Zhinko");
        resume.setContact(ContactType.MAIL, "keshondzin@gmail.com");
        resume.setContact(ContactType.PHONE, "+275336363646");
        resume.setSection(SectionType.OBJECTIVE, section);
        resume.setSection(SectionType.PERSONAL, section1);
        resume.setSection(SectionType.ACHIEVEMENT, section2);
        resume.setSection(SectionType.EXPERIENCE, companySection);

        System.out.println(resume.getFullName());
        for(ContactType contactType: ContactType.values()){
            System.out.println(resume.getContact(contactType));
        }
        for(SectionType sectionType: SectionType.values()){
            System.out.println(resume.getSection(sectionType));
        }
    }
}