package org.monet.sql.agents;

import java.sql.Connection;

public interface Agent {
  String getSchema(Connection connection) throws Exception ;
}
