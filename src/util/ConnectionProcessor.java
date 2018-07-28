package util;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface ConnectionProcessor {
   void process(PreparedStatement ps) throws SQLException;
}
