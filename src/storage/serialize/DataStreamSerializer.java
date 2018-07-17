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
        Writable<String> writeString = dos::writeUTF;
        Writable<Integer> writeInt = dos::writeInt;
        switch (sectionType.getTitle()) {
            case "Личные качества":
            case "Позиция":
                TextSection textSection = (TextSection) section;
                writeString.write(textSection.getContent());
                break;
            case "Достижения":
            case "Квалификация":
                ListSection listSection = (ListSection) section;
                List<String> contents = listSection.getContents();
                writeInt.write(contents.size());
                writeCollection(contents, dos::writeUTF);
                break;
            case "Опыт работы":
            case "Образование":
                CompanySection companySection = (CompanySection) section;
                List<Company> companies = companySection.getCompanies();
                writeInt.write(companies.size());
                writeCollection(companies, company -> {
                    Link link = company.getLink();
                    writeString.write(link.getName());
                    writeString.write(link.getUrl());
                    List<Company.Post> posts = company.getPostList();
                    writeInt.write(posts.size());
                    writeCollection(posts, post -> {
                        writeString.write(post.getPosition());
                        LocalDate startWork = post.getStartWork();
                        writeInt.write(startWork.getYear());
                        writeInt.write(startWork.getMonth().getValue());
                        LocalDate endWork = post.getEndWork();
                        writeInt.write(endWork.getYear());
                        writeInt.write(endWork.getMonth().getValue());
                        writeString.write(post.getDescription());
                    });
                });
                break;
        }
    }

    private Section readSection(DataInputStream dis, SectionType sectionType) throws IOException {
        Readable<String> readString = dis::readUTF;
        Readable<Integer> readInt = dis::readInt;
        switch (sectionType.getTitle()) {
            case "Личные качества":
            case "Позиция":
                TextSection textSection = new TextSection();
                textSection.setContent(readString.read());
                return textSection;
            case "Достижения":
            case "Квалификация":
                ListSection listSection = new ListSection();
                listSection.setContents(readCollection(readInt.read(), readString));
                return listSection;
            case "Опыт работы":
            case "Образование":
                CompanySection companySection = new CompanySection();
                List<Company> companies = readCollection(readInt.read(), () -> {
                    Link link = new Link(readString.read(), readString.read());
                    List<Company.Post> posts = readCollection(readInt.read(), () -> {
                        String position = readString.read();
                        LocalDate startWork = DateUtil.of(readInt.read(), Month.of(readInt.read()));
                        LocalDate endWork = DateUtil.of(readInt.read(), Month.of(readInt.read()));
                        String description = readString.read();
                        return new Company.Post(position, startWork, endWork, description);
                    });
                    return new Company(link, posts);
                });
                companySection.setCompanies(companies);
                return companySection;
        }
        return null;
    }

    public interface Writable<T> {
        void write(T t) throws IOException;
    }

    public interface Readable<T> {
        T read() throws IOException;
    }

    private <T> void writeCollection(Collection<T> collection, Writable<T> writable) throws IOException {
        for (T element : collection) {
            writable.write(element);
        }
    }

    private <T> List<T> readCollection(int size, Readable<T> readable) throws IOException {
        ArrayList<T> result = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            result.add(readable.read());
        }
        return result;
    }
}