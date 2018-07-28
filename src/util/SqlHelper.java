package util;

import exception.ExistStorageException;
import exception.StorageException;
import sql.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlHelper {
    public static void connectionExecutor(ConnectionFactory connectionFactory, String query, String uuid, ConnectionProcessor connectionProcessor) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            connectionProcessor.process(ps);
        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) {
                throw new ExistStorageException(uuid);
            } else throw new StorageException(e);
        }
    }
}
