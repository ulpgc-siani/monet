package org.monet.sql.agents.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.monet.sql.agents.Agent;

public class AgentMySQL implements Agent{

  @Override
  public String getSchema(Connection connection) throws Exception {
    String databaseUrl;
    String schemaName = "";
    Properties props;
    
    try {
      databaseUrl = connection.getMetaData().getURL();
      props = new com.mysql.jdbc.NonRegisteringDriver().parseURL(databaseUrl, null);
      schemaName = props.getProperty(com.mysql.jdbc.Driver.DBNAME_PROPERTY_KEY);
    }
    catch (SQLException ex) {
      throw ex;
    }

    return schemaName;
  }

}
