package storage.serialize;

import model.*;
import util.DateUtil;

import java.io.*;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collection;
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
            writeValues(contacts.entrySet(), entry -> {
                dos.writeUTF(entry.getKey().name());
                dos.writeUTF(entry.getValue());
            });
            Map<SectionType, Section> sections = resume.getSections();
            dos.writeInt(sections.size());
            writeValues(sections.entrySet(), entry -> {
                SectionType sectionType = entry.getKey();
                dos.writeUTF(sectionType.name());
                writeSection(dos, sectionType, entry.getValue());
            });
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
                TextSection textSection = (TextSection) section;
                dos.writeUTF(textSection.getContent());
                break;
            case "Достижения":
            case "Квалификация":
                ListSection listSection = (ListSection) section;
                List<String> contents = listSection.getContents();
                dos.writeInt(contents.size());
                writeValues(contents, dos::writeUTF);
                break;
            case "Опыт работы":
            case "Образование":
                CompanySection companySection = (CompanySection) section;
                List<Company> companies = companySection.getCompanies();
                dos.writeInt(companies.size());
                writeValues(companies, company -> {
                    Link link = company.getLink();
                    dos.writeUTF(link.getName());
                    dos.writeUTF(link.getUrl() == null ? "null" : link.getUrl());
                    List<Company.Post> posts = company.getPostList();
                    dos.writeInt(posts.size());
                    writeValues(posts, post -> {
                        dos.writeUTF(post.getPosition());
                        LocalDate startWork = post.getStartWork();
                        dos.writeInt(startWork.getYear());
                        dos.writeInt(startWork.getMonth().getValue());
                        LocalDate endWork = post.getEndWork();
                        dos.writeInt(endWork.getYear());
                        dos.writeInt(endWork.getMonth().getValue());
                        dos.writeUTF(post.getDescription() == null ? "null" : post.getDescription());
                    });
                });
                break;
        }
    }

    private Section readSection(DataInputStream dis, SectionType sectionType) throws IOException {
        switch (sectionType.getTitle()) {
            case "Личные качества":
            case "Позиция":
                TextSection textSection = new TextSection();
                textSection.setContent(dis.readUTF());
                return textSection;
            case "Достижения":
            case "Квалификация":
                ListSection listSection = new ListSection();
                listSection.setContents(readValues(dis.readInt(), dis::readUTF));
                return listSection;
            case "Опыт работы":
            case "Образование":
                CompanySection companySection = new CompanySection();
                List<Company> companies = readValues(dis.readInt(), () -> {
                    String name = dis.readUTF();
                    String url = dis.readUTF();
                    Link link = url.equals("null") ? new Link(name, null) : new Link(name, url);
                    List<Company.Post> posts = readValues(dis.readInt(), () -> {
                        String position = dis.readUTF();
                        LocalDate startWork = DateUtil.of(dis.readInt(), Month.of(dis.readInt()));
                        LocalDate endWork = DateUtil.of(dis.readInt(), Month.of(dis.readInt()));
                        String description = dis.readUTF();
                        if (description.equals("null")) {
                            description = null;
                        }
                        return new Company.Post(position, startWork, endWork, description);
                    });
                    return new Company(link, posts);
                });
                companySection.setCompanies(companies);
                return companySection;
        }
        return null;
    }

    private interface Writable<T> {
        void write(T t) throws IOException;
    }

    private interface Readable<T> {
        T read() throws IOException;
    }

    private <T> void writeValues(Collection<T> collection, Writable<T> writable) throws IOException {
        for (T element : collection) {
            writable.write(element);
        }
    }

    private <T> List<T> readValues(int size, Readable<T> readable) throws IOException {
        ArrayList<T> result = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            result.add(readable.read());
        }
        return result;
    }
}