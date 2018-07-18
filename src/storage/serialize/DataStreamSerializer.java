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
            writeValues(dos, contacts.entrySet(), entry -> {
                dos.writeUTF(entry.getKey().name());
                dos.writeUTF(entry.getValue());
            }, contacts.size());
            Map<SectionType, Section> sections = resume.getSections();
            writeValues(dos, sections.entrySet(), entry -> {
                SectionType sectionType = entry.getKey();
                dos.writeUTF(sectionType.name());
                writeSection(dos, sectionType, entry.getValue());
            }, sections.size());
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
                writeValues(dos, contents, dos::writeUTF, contents.size());
                break;
            case "Опыт работы":
            case "Образование":
                CompanySection companySection = (CompanySection) section;
                List<Company> companies = companySection.getCompanies();
                writeValues(dos, companies, company -> {
                    Link link = company.getLink();
                    dos.writeUTF(link.getName());
                    dos.writeUTF(link.getUrl() == null ? "null" : link.getUrl());
                    List<Company.Post> posts = company.getPostList();
                    writeValues(dos, posts, post -> {
                        dos.writeUTF(post.getPosition());
                        LocalDate startWork = post.getStartWork();
                        dos.writeInt(startWork.getYear());
                        dos.writeInt(startWork.getMonth().getValue());
                        LocalDate endWork = post.getEndWork();
                        dos.writeInt(endWork.getYear());
                        dos.writeInt(endWork.getMonth().getValue());
                        dos.writeUTF(post.getDescription() == null ? "null" : post.getDescription());
                    }, posts.size());
                }, companies.size());
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
                listSection.setContents(readValues(dis, dis::readUTF));
                return listSection;
            case "Опыт работы":
            case "Образование":
                CompanySection companySection = new CompanySection();
                List<Company> companies = readValues(dis, () -> {
                    String name = dis.readUTF();
                    String url = dis.readUTF();
                    Link link = url.equals("null") ? new Link(name, null) : new Link(name, url);
                    List<Company.Post> posts = readValues(dis, () -> {
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

    private <T> void writeValues(DataOutputStream dos, Collection<T> collection, Writable<T> writable, int size) throws IOException {
        dos.writeInt(size);
        for (T element : collection) {
            writable.write(element);
        }
    }

    private <T> List<T> readValues(DataInputStream dis, Readable<T> readable) throws IOException {
        int size = dis.readInt();
        ArrayList<T> result = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            result.add(readable.read());
        }
        return result;
    }
}