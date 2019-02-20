package org.monet.sql.agents.impl;

import java.sql.Connection;

import org.monet.sql.agents.Agent;

public class AgentOracle implements Agent {

  @Override
  public String getSchema(Connection connection) throws Exception {
    return "NoSchema";
  }

}
