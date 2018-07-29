package util;

import exception.ExistStorageException;
import exception.StorageException;
import sql.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlHelper {
    private static ConnectionFactory connectionFactory;

    public static <T> T connectionExecutor(String query, String uuid, ConnectionProcessor<T> connectionProcessor) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            return connectionProcessor.process(ps);
        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) {
                throw new ExistStorageException(uuid);
            } else throw new StorageException(e);
        }
    }

    public static <T> T connectionExecutor(String query, ConnectionProcessor<T> connectionProcessor) {
       return connectionExecutor(query, null, connectionProcessor);
    }

    public static void setConnectionFactory(ConnectionFactory connectionFactory) {
        SqlHelper.connectionFactory = connectionFactory;
    }
}
