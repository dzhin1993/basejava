package storage.serialize;

import model.*;

import java.io.*;
import java.time.LocalDate;
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
            readItems(dis, () -> resume.setContact(ContactType.valueOf(dis.readUTF()), dis.readUTF()));
            readItems(dis, () -> {
                SectionType sectionType = SectionType.valueOf(dis.readUTF());
                resume.setSection(sectionType, readSection(dis, sectionType));
            });
            return resume;
        }
    }

    private void writeSection(DataOutputStream dos, SectionType sectionType, Section section) throws IOException {
        switch (sectionType) {
            case PERSONAL:
            case OBJECTIVE:
                dos.writeUTF(((TextSection) section).getContent());
                break;
            case ACHIEVEMENT:
            case QUALIFICATIONS:
                List<String> contents = ((ListSection) section).getContents();
                writeValues(dos, contents, dos::writeUTF, contents.size());
                break;
            case EXPERIENCE:
            case EDUCATION:
                CompanySection companySection = (CompanySection) section;
                List<Company> companies = companySection.getCompanies();
                writeValues(dos, companies, company -> {
                    Link link = company.getLink();
                    dos.writeUTF(link.getName());
                    dos.writeUTF(link.getUrl());
                    List<Company.Post> posts = company.getPostList();
                    writeValues(dos, posts, post -> {
                        dos.writeUTF(post.getPosition());
                        writeLocalDate(dos, post.getStartWork());
                        writeLocalDate(dos, post.getEndWork());
                        dos.writeUTF(post.getDescription());
                    }, posts.size());
                }, companies.size());
                break;
        }
    }

    private Section readSection(DataInputStream dis, SectionType sectionType) throws IOException {
        switch (sectionType) {
            case PERSONAL:
            case OBJECTIVE:
                return new TextSection(dis.readUTF());
            case ACHIEVEMENT:
            case QUALIFICATIONS:
                return new ListSection(readValues(dis, dis::readUTF));
            case EXPERIENCE:
            case EDUCATION:
                return new CompanySection(readValues(dis, () -> {
                    Link link = new Link(dis.readUTF(), dis.readUTF());
                    return new Company(link, readValues(dis, () ->
                            new Company.Post(dis.readUTF(), readLocalDate(dis), readLocalDate(dis), dis.readUTF())));
                }));
        }
        return null;
    }

    @FunctionalInterface
    private interface Writable<T> {
        void write(T t) throws IOException;
    }

    @FunctionalInterface
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

    private void readItems(DataInputStream dis, ElementProcessor processor) throws IOException {
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            processor.process();
        }
    }

    @FunctionalInterface
    private interface ElementProcessor {
        void process() throws IOException;
    }

    private void writeLocalDate(DataOutputStream dos, LocalDate ld) throws IOException {
        dos.writeInt(ld.getYear());
        dos.writeInt(ld.getMonth().getValue());
    }

    private LocalDate readLocalDate(DataInputStream dis) throws IOException {
        return LocalDate.of(dis.readInt(), dis.readInt(), 1);
    }
}