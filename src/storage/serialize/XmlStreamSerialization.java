package storage.serialize;

import model.*;
import util.XmlParser;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class XmlStreamSerialization implements SerializeStrategy {

    private XmlParser xmlParser;

    public XmlStreamSerialization() {
        xmlParser = new XmlParser(
                Resume.class, Company.class, Link.class,
                CompanySection.class, TextSection.class, ListSection.class, Company.Post.class);
    }

    @Override
    public void doWrite(Resume resume, OutputStream os) throws IOException {
        try (Writer writer = new OutputStreamWriter(os, StandardCharsets.UTF_8)){
            xmlParser.marshall(resume, writer);
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
       try (Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)){
           return  xmlParser.unmarshall(reader);
       }
    }
}
//--add-modules java.xml.bind