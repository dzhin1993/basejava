package storage.serialize;

import model.*;
import util.DateUtil;

import java.io.*;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataStreamSerializer implements SerializeStrategy {

    @Override
    public void doWrite(Resume resume, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(resume.getUuid());
            dos.writeUTF(resume.getFullName());
            Map<ContactType, String> contacts = resume.getContacts();
            dos.writeInt(contacts.size());
            for (Map.Entry<ContactType, String> entry : contacts.entrySet()) {
                dos.writeUTF(entry.getKey().name());
                dos.writeUTF(entry.getValue());
            }
            Map<SectionType, Section> sections = resume.getSections();
            dos.writeInt(sections.size());
            for (Map.Entry<SectionType, Section> entry : sections.entrySet()) {
                SectionType sectionType = entry.getKey();
                dos.writeUTF(sectionType.name());
                writeSection(dos, sectionType, entry.getValue());
            }
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();
            Resume resume = new Resume(uuid, fullName);
            int contactsSize = dis.readInt();
            for (int i = 0; i < contactsSize; i++) {
                resume.setContact(ContactType.valueOf(dis.readUTF()), dis.readUTF());
            }
            int sectionsSize = dis.readInt();
            for (int i = 0; i < sectionsSize; i++) {
                SectionType sectionType = SectionType.valueOf(dis.readUTF());
                resume.setSection(sectionType, readSection(dis, sectionType));
            }
            return resume;
        }
    }

    private void writeSection(DataOutputStream dos, SectionType sectionType, Section section) throws IOException {
        switch (sectionType.getTitle()) {
            case "Личные качества":
            case "Позиция":
                textSectionWriter(dos, section);
                break;
            case "Достижения":
            case "Квалификация":
                listSectionWriter(dos, section);
                break;
            case "Опыт работы":
            case "Образование":
                companySectionWriter(dos, section);
                break;
        }
    }

    private Section readSection(DataInputStream dis, SectionType sectionType) throws IOException {
        switch (sectionType.getTitle()) {
            case "Личные качества":
            case "Позиция":
                return textSectionReader(dis);
            case "Достижения":
            case "Квалификация":
                return listSectionReader(dis);
            case "Опыт работы":
            case "Образование":
                return companySectionReader(dis);
        }
        return null;
    }

    private void textSectionWriter(DataOutputStream dos, Section section) throws IOException {
        TextSection textSection = (TextSection) section;
        dos.writeUTF(textSection.getContent());
    }

    private TextSection textSectionReader(DataInputStream dis) throws IOException {
        TextSection textSection = new TextSection();
        textSection.setContent(dis.readUTF());
        return textSection;
    }

    private void listSectionWriter(DataOutputStream dos, Section section) throws IOException {
        ListSection listSection = (ListSection) section;
        List<String> contents = listSection.getContents();
        dos.writeInt(contents.size());
        for (String content : contents) {
            dos.writeUTF(content);
        }
    }

    private ListSection listSectionReader(DataInputStream dis) throws IOException {
        ListSection listSection = new ListSection();
        List<String> contents = new ArrayList<>();
        int contentsSize = dis.readInt();
        for (int k = 0; k < contentsSize; k++) {
            contents.add(dis.readUTF());
        }
        listSection.setContents(contents);
        return listSection;
    }

    private void companySectionWriter(DataOutputStream dos, Section section) throws IOException {
        CompanySection companySection = (CompanySection) section;
        List<Company> companies = companySection.getCompanies();
        dos.writeInt(companies.size());
        for (Company company : companies) {
            Link link = company.getLink();
            dos.writeUTF(link.getName());
            dos.writeUTF(link.getUrl());
            List<Company.Post> posts = company.getPostList();
            dos.writeInt(posts.size());
            for (Company.Post post : posts) {
                dos.writeUTF(post.getPosition());
                LocalDate startWork = post.getStartWork();
                dos.writeInt(startWork.getYear());
                dos.writeInt(startWork.getMonth().getValue());
                LocalDate endWork = post.getEndWork();
                dos.writeInt(endWork.getYear());
                dos.writeInt(endWork.getMonth().getValue());
                dos.writeUTF(post.getDescription());
            }
        }
    }

    private CompanySection companySectionReader(DataInputStream dis) throws IOException {
        CompanySection companySection = new CompanySection();
        List<Company> companies = new ArrayList<>();
        int companiesSize = dis.readInt();
        for (int i = 0; i < companiesSize; i++) {
            Link link = new Link(dis.readUTF(), dis.readUTF());
            List<Company.Post> posts = new ArrayList<>();
            int postsSize = dis.readInt();
            for (int n = 0; n < postsSize; n++) {
                String position = dis.readUTF();
                LocalDate startWork = DateUtil.of(dis.readInt(), Month.of(dis.readInt()));
                LocalDate endWork = DateUtil.of(dis.readInt(), Month.of(dis.readInt()));
                String description = dis.readUTF();
                posts.add(new Company.Post(position, startWork, endWork, description));
            }
            Company company = new Company(link, posts);
            companies.add(company);
        }
        companySection.setCompanies(companies);
        return companySection;
    }
}