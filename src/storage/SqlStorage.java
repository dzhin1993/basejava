package storage;

import exception.NotExistStorageException;
import model.Resume;
import sql.ConnectionFactory;
import util.SqlHelper;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SqlStorage implements Storage {
    private final ConnectionFactory connectionFactory;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        connectionFactory = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    @Override
    public void clear() {
        SqlHelper.connectionExecutor(connectionFactory, "DELETE FROM resume", null, PreparedStatement::execute);
    }

    @Override
    public Resume get(String uuid) {
        Resume resume = new Resume();
        SqlHelper.connectionExecutor(connectionFactory, "SELECT * FROM resume r WHERE r.uuid =?", uuid, ps -> {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException(uuid);
            }
            resume.setUuid(uuid);
            resume.setFullName(rs.getString("full_name"));
        });
        return resume;
    }

    @Override
    public void update(Resume r) {
        SqlHelper.connectionExecutor(connectionFactory, "UPDATE resume SET full_name = ? WHERE uuid = ?", r.getUuid(), ps -> {
            ps.setString(1, r.getFullName());
            ps.setString(2, r.getUuid());
            if (ps.executeUpdate() == 0) {
                throw new NotExistStorageException(r.getUuid());
            }
        });
    }

    @Override
    public void save(Resume r) {
        SqlHelper.connectionExecutor(connectionFactory, "INSERT INTO resume (uuid, full_name) VALUES (?,?)", r.getUuid(), ps -> {
            ps.setString(1, r.getUuid());
            ps.setString(2, r.getFullName());
            ps.execute();
        });
    }

    @Override
    public void delete(String uuid) {
        SqlHelper.connectionExecutor(connectionFactory, "DELETE FROM resume r WHERE r.uuid=?", uuid, ps -> {
            ps.setString(1, uuid);
            if (ps.executeUpdate() == 0) {
                throw new NotExistStorageException(uuid);
            }
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        List<Resume> resumeList = new ArrayList<>();
        SqlHelper.connectionExecutor(connectionFactory, "SELECT * FROM resume", null, ps -> {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Resume resume = new Resume(rs.getString("uuid").trim(), rs.getString("full_name"));
                resumeList.add(resume);
            }
        });
        resumeList.sort(Comparator.comparing(Resume::getFullName).thenComparing(Resume::getUuid));
        return resumeList;
    }

    @Override
    public int size() {
        ArrayList<Integer> list = new ArrayList<>();
        SqlHelper.connectionExecutor(connectionFactory, "SELECT * FROM resume", null, ps -> {
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                list.add(1);
            }
        });
        return list.size();
    }
}