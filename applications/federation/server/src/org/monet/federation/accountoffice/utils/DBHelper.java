package org.monet.federation.accountoffice.utils;

import java.sql.Connection;
import java.sql.ResultSet;

public class DBHelper {

  public static final void close(Connection connection) {
    if (connection != null)
      try {
        if (!connection.isClosed()) {
          connection.setAutoCommit(true);
          connection.close();
        }
      } catch (Throwable e) {
      }
  }

  public static final void close(NamedParameterStatement statement) {
    if (statement != null)
      try {
        statement.close();
      } catch (Throwable e) {
      }
  }

  public static final void close(ResultSet result) {
    if (result != null)
      try {
        result.close();
      } catch (Throwable e) {
      }
  }

}
