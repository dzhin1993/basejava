package sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface ConnectionProcessor<T>  {
  T process(PreparedStatement ps) throws SQLException;
}
