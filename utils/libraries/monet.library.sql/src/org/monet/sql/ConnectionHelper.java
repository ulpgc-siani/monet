package org.monet.sql;

import java.sql.Connection;
import java.sql.ResultSet;

public class ConnectionHelper {
  public static void close(Connection connection) throws Exception {
    if(connection != null){
      connection.setAutoCommit(true);
      connection.close();
    }
  }

  public static void close(NamedParameterStatement statement) throws Exception {
    if(statement != null)
      statement.close();
  }
  
  public static void close(java.sql.Statement statement) throws Exception {
    if(statement != null)
      statement.close();
  }

  public static void close(ResultSet documentDataCount) throws Exception {
    if(documentDataCount != null)
      documentDataCount.close();
  }

}
