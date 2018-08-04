package storage;

import exception.NotExistStorageException;
import model.ContactType;
import model.Resume;
import sql.SqlHelper;

import java.sql.*;
import java.util.*;

public class SqlStorage implements Storage {

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        SqlHelper.setConnectionFactory(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    @Override
    public void clear() {
        SqlHelper.connectionExecutor("DELETE FROM resume", PreparedStatement::execute);
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
                addContacts(rs, r);
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
                    insertContacts(conn, r, "UPDATE contact SET type = ?, value = ? WHERE resume_uuid = ?");
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
                    insertContacts(conn, r, "INSERT INTO contact (resume_uuid, type, value) VALUES (?,?,?)");
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
        return SqlHelper.connectionExecutor(""  +
                "SELECT * FROM resume r " +
                "LEFT JOIN contact c " +
                "ON r.uuid = c.resume_uuid " +
                "ORDER BY full_name, uuid", ps -> {
            HashMap<String, Resume> resumeHashMap = new HashMap<>();
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String uuid = rs.getString("uuid");
                if (resumeHashMap.get(uuid) == null) {
                    Resume resume = new Resume(uuid, rs.getString("full_name"));
                    resumeHashMap.put(uuid, resume);
                    addContacts(rs, resume);
                } else {
                    Resume resume = resumeHashMap.get(uuid);
                    addContacts(rs, resume);
                }
            }
            ArrayList<Resume> list = new ArrayList<>(resumeHashMap.values());
            list.sort(Comparator.comparing(Resume::getFullName).thenComparing(Resume::getUuid));
            return list;
        });
    }

    @Override
    public int size() {
        return SqlHelper.connectionExecutor("SELECT COUNT(*) FROM resume", ps -> {
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        });
    }

    private void insertContacts(Connection conn, Resume r, String query) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            for (Map.Entry<ContactType, String> e : r.getContacts().entrySet()) {
                ps.setString(1, r.getUuid());
                ps.setString(2, e.getKey().name());
                ps.setString(3, e.getValue());
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

    private void addContacts(ResultSet rs, Resume r) throws SQLException {
        String type = rs.getString("type");
        String value = rs.getString("value");
        if (value != null) {
            r.setContact(ContactType.valueOf(type), value);
        }
    }
}