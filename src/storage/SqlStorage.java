package storage;

import exception.NotExistStorageException;
import model.ContactType;
import model.Resume;
import sql.SqlHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SqlStorage implements Storage {

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        SqlHelper.setConnectionFactory(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    @Override
    public void clear() {
        SqlHelper.connectionExecutor("DELETE FROM resume", PreparedStatement::execute);
        SqlHelper.connectionExecutor("DELETE FROM contact", PreparedStatement::execute);
    }

    @Override
    public Resume get(String uuid) {
        return SqlHelper.connectionExecutor("" +
                "    SELECT * FROM resume r " +
                " LEFT JOIN contact c " +
                "        ON r.uuid = c.resume_uuid " +
                "     WHERE r.uuid =? ", ps -> {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException(uuid);
            }
            Resume r = new Resume(uuid, rs.getString("full_name"));
            do {
                String type = rs.getString("type");
                String value = rs.getString("value");
                if ((type != null) && (value != null)) {
                    r.setContact(ContactType.valueOf(type), value);
                }
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
                    insertContacts(conn.prepareStatement("UPDATE contact SET type = ?, value = ? WHERE resume_uuid = ?"), r);
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
                    insertContacts(conn.prepareStatement("INSERT INTO contact (resume_uuid, type, value) VALUES (?,?,?)"), r);
                    return null;
                }
        );
    }

    @Override
    public void delete(String uuid) {
        SqlHelper.transactionalExecute(conn -> {
                    try (PreparedStatement ps = conn.prepareStatement("DELETE FROM resume r WHERE r.uuid=?")) {
                        ps.setString(1, uuid);
                        if (ps.executeUpdate() == 0) {
                            throw new NotExistStorageException(uuid);
                        }
                    }
                    deleteContacts(conn, uuid);
                    return null;
                }
        );
    }

    @Override
    public List<Resume> getAllSorted() {
        return SqlHelper.transactionalExecute(conn -> {
                    List<Resume> resumeList = new ArrayList<>();
                    try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM resume ORDER BY full_name, uuid")) {
                        ResultSet rs = ps.executeQuery();
                        while (rs.next()) {
                            Resume resume = new Resume();
                            String uuid = rs.getString("uuid");
                            resume.setUuid(uuid.trim());
                            resume.setFullName(rs.getString("full_name"));
                            try (PreparedStatement ps2 = conn.prepareStatement("SELECT * FROM contact c WHERE c.resume_uuid=?")) {
                                ps2.setString(1, uuid);
                                ResultSet rs2 = ps2.executeQuery();
                                while (rs2.next()) {
                                    String type = rs2.getString("type");
                                    String value = rs2.getString("value");
                                    resume.setContact(ContactType.valueOf(type), value);
                                }
                            }
                            resumeList.add(resume);
                        }
                    }
                    return resumeList;
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

    private void insertContacts(PreparedStatement ps, Resume r) throws SQLException {
        for (Map.Entry<ContactType, String> e : r.getContacts().entrySet()) {
            ps.setString(1, r.getUuid());
            ps.setString(2, e.getKey().name());
            ps.setString(3, e.getValue());
            ps.addBatch();
        }
        ps.executeBatch();
    }

    private void deleteContacts(Connection conn, String uuid) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM contact WHERE resume_uuid = ?")) {
            ps.setString(1, uuid);
            ps.execute();
        }
    }
}