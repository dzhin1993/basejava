package sql;

import exception.ExistStorageException;
import exception.StorageException;
import org.postgresql.util.PSQLException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlHelper {
    private static ConnectionFactory connectionFactory;

    public static void setConnectionFactory(ConnectionFactory connectionFactory) {
        SqlHelper.connectionFactory = connectionFactory;
    }

    public static <T> T connectionExecutor(String query, ConnectionProcessor<T> connectionProcessor) {
        return connectionExecutor(query, null, connectionProcessor);
    }

    public static <T> T connectionExecutor(String query, String uuid, ConnectionProcessor<T> connectionProcessor) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            return connectionProcessor.process(ps);
        } catch (SQLException e) {
            throw convertException(e, uuid);
        }
    }

    public static  <T> T transactionalExecute(SqlTransaction<T> executor) {
        try (Connection conn = connectionFactory.getConnection()) {
            try {
                conn.setAutoCommit(false);
                T res = executor.execute(conn);
                conn.commit();
                return res;
            } catch (SQLException e) {
                conn.rollback();
                throw convertException(e, null);
            }
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    private static StorageException convertException(SQLException e, String uuid) {
        if (e instanceof PSQLException) {
            if (e.getSQLState().equals("23505")) {
                return new ExistStorageException(uuid);
            }
        }
        return new StorageException(e);
    }
}
