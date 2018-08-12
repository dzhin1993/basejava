package storage;

import exception.NotExistStorageException;
import exception.StorageException;
import model.*;
import sql.SqlHelper;

import java.sql.*;
import java.util.*;

public class SqlStorage implements Storage {

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new StorageException("PostgreSQL DataSource unable to load PostgreSQL JDBC Driver");
        }
        SqlHelper.setConnectionFactory(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    @Override
    public void clear() {
        SqlHelper.connectionExecutor("DELETE FROM resume", PreparedStatement::execute);
    }

    @Override
    public Resume get(String uuid) {
        return SqlHelper.connectionExecutor("" +
                "SELECT * FROM resume r " +
                "LEFT JOIN contact c " +
                "ON r.uuid = c.resume_uuid " +
                "LEFT JOIN section s " +
                "ON r.uuid = s.resume_uuid " +
                "WHERE r.uuid = ?", ps -> {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException(uuid);
            }
            Resume r = new Resume(uuid, rs.getString("full_name"));
            do {
                addContacts(rs, r);
                addSections(rs, r);
            } while (rs.next());
            return r;
        });
    }

    @Override
    public void update(Resume r) {
        SqlHelper.transactionalExecute(conn -> {
                    try (PreparedStatement ps = conn.prepareStatement("UPDATE resume SET full_name = ? WHERE uuid = ?")) {
                        ps.setString(1, r.getFullName());
                        ps.setString(2, r.getUuid());
                        if (ps.executeUpdate() == 0) {
                            throw new NotExistStorageException(r.getUuid());
                        }
                    }
                    deleteContacts(conn, r.getUuid());
                    deleteSections(conn, r.getUuid());
                    insertContacts(conn, r);
                    insertSections(conn, r);
                    return null;
                }
        );
    }

    @Override
    public void save(Resume r) {
        SqlHelper.transactionalExecute(conn -> {
                    try (PreparedStatement ps = conn.prepareStatement("INSERT INTO resume (uuid, full_name) VALUES (?,?)")) {
                        ps.setString(1, r.getUuid());
                        ps.setString(2, r.getFullName());
                        ps.execute();
                    }
                    insertContacts(conn, r);
                    insertSections(conn, r);
                    return null;
                }
        );
    }

    @Override
    public void delete(String uuid) {
        SqlHelper.connectionExecutor("DELETE FROM resume r WHERE r.uuid=?", ps -> {
            ps.setString(1, uuid);
            if (ps.executeUpdate() == 0) {
                throw new NotExistStorageException(uuid);
            }
            return null;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        return SqlHelper.transactionalExecute(conn -> {
                    Map<String, Resume> resumeHashMap = new LinkedHashMap<>();
                    try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM resume ORDER BY full_name, uuid")) {
                        ResultSet rs = ps.executeQuery();
                        while (rs.next()) {
                            String uuid = rs.getString("uuid");
                            Resume resume = new Resume(uuid, rs.getString("full_name"));
                            resumeHashMap.put(uuid, resume);
                        }
                    }
                    try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM contact")) {
                        ResultSet rs = ps.executeQuery();
                        while (rs.next()) {
                            String uuid = rs.getString("resume_uuid");
                            Resume resume = resumeHashMap.get(uuid);
                            addContacts(rs, resume);
                        }
                    }
                    try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM section")) {
                        ResultSet rs = ps.executeQuery();
                        while (rs.next()) {
                            String uuid = rs.getString("resume_uuid");
                            Resume resume = resumeHashMap.get(uuid);
                            addSections(rs, resume);
                        }
                    }
                    return new ArrayList<>(resumeHashMap.values());
                }
        );
    }

    @Override
    public int size() {
        return SqlHelper.connectionExecutor("SELECT COUNT(*) FROM resume", ps -> {
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        });
    }

    private void insertContacts(Connection conn, Resume r) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO contact (resume_uuid, type, value) VALUES (?,?,?)")) {
            for (Map.Entry<ContactType, String> e : r.getContacts().entrySet()) {
                ps.setString(1, r.getUuid());
                ps.setString(2, e.getKey().name());
                ps.setString(3, e.getValue());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void insertSections(Connection conn, Resume r) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO section (resume_uuid, section_type, section_value) VALUES (?,?,?)")) {
            for (Map.Entry<SectionType, Section> e : r.getSections().entrySet()) {
                ps.setString(1, r.getUuid());
                ps.setString(2, e.getKey().name());
                ps.setString(3, getSectionContent(e.getKey(), e.getValue()));
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void deleteContacts(Connection conn, String uuid) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM contact WHERE resume_uuid = ?")) {
            ps.setString(1, uuid);
            ps.execute();
        }
    }

    private void deleteSections(Connection conn, String uuid) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM section WHERE resume_uuid = ?")) {
            ps.setString(1, uuid);
            ps.execute();
        }
    }

    private void addContacts(ResultSet rs, Resume r) throws SQLException {
        String type = rs.getString("type");
        String value = rs.getString("value");
        if (value != null) {
            r.setContact(ContactType.valueOf(type), value);
        }
    }

    private void addSections(ResultSet rs, Resume r) throws SQLException {
        String type = rs.getString("section_type");
        String value = rs.getString("section_value");
        if (value != null) {
            Section section;
            String[] contents = value.split("\n");
            if (contents.length == 1) {
                section = new TextSection(value);
            } else {
                List<String> contentList = new ArrayList<>(Arrays.asList(contents));
                section = new ListSection(contentList);
            }
            r.setSection(SectionType.valueOf(type), section);
        }
    }

    private String getSectionContent(SectionType sectionType, Section section) {
        switch (sectionType) {
            case PERSONAL:
            case OBJECTIVE:
                return ((TextSection) section).getContent();
            case ACHIEVEMENT:
            case QUALIFICATIONS:
                List<String> contents = ((ListSection) section).getContents();
                return String.join("\n", contents);
        }
        return null;
    }
}