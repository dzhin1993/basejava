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
            SectionType[] sectionTypes = SectionType.values();
            for (int i = 0; i < sectionTypes.length; i++) {
                boolean isSectionExist = resume.getSection(sectionTypes[i]) != null;
                dos.writeUTF(String.valueOf(isSectionExist));
                if (isSectionExist) {
                    dos.writeUTF(sectionTypes[i].name());
                    Section section = resume.getSection(sectionTypes[i]);
                    if (i < 2) {
                        TextSection textSection = (TextSection) section;
                        dos.writeUTF(textSection.getContent());
                    } else if (i < 4) {
                        ListSection listSection = (ListSection) section;
                        List<String> contents = listSection.getContents();
                        dos.writeInt(contents.size());
                        for (String content : contents) {
                            dos.writeUTF(content);
                        }
                    } else {
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
                }
            }
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();
            Resume resume = new Resume(uuid, fullName);
            int size = dis.readInt();
            for (int i = 0; i < size; i++) {
                resume.setContact(ContactType.valueOf(dis.readUTF()), dis.readUTF());
            }
            SectionType[] sectionTypes = SectionType.values();
            for (int i = 0; i < sectionTypes.length; i++) {
                if (Boolean.valueOf(dis.readUTF())) {
                    SectionType sectionType = SectionType.valueOf(dis.readUTF());
                    if (i < 2) {
                        TextSection textSection = new TextSection();
                        textSection.setContent(dis.readUTF());
                        resume.setSection(sectionType, textSection);
                    } else if (i < 4) {
                        ListSection listSection = new ListSection();
                        List<String> contents = new ArrayList<>();
                        int contentsSize = dis.readInt();
                        for (int k = 0; k < contentsSize; k++) {
                            contents.add(dis.readUTF());
                        }
                        listSection.setContents(contents);
                        resume.setSection(sectionType, listSection);
                    } else {
                        CompanySection companySection = new CompanySection();
                        List<Company> companies = new ArrayList<>();
                        int companiesSize = dis.readInt();
                        for (int k = 0; k < companiesSize; k++) {
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
                        resume.setSection(sectionType, companySection);
                    }
                }
            }
            return resume;
        }
    }

   /*private void textSectionReader(DataOutputStream dos, Section section) throws IOException {
       TextSection textSection = (TextSection) section;
       dos.writeUTF(textSection.getContent());
   }

   private void listSectionReader(DataOutputStream dos, Section section) throws IOException {
       ListSection listSection = (ListSection) section;
       List<String> contents = listSection.getContents();
       dos.writeInt(contents.size());
       for (String content : contents) {
           dos.writeUTF(content);
       }
   }

   private void CompanySection(DataOutputStream dos, Section section) throws IOException {

   }*/
}
