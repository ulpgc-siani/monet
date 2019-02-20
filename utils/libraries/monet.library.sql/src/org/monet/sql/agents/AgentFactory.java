package org.monet.sql.agents;

import java.util.HashMap;
import java.util.Map;

import org.monet.sql.agents.impl.AgentMySQL;
import org.monet.sql.agents.impl.AgentOracle;

public class AgentFactory {
  public static final String MYSQL_AGENT = "mysql";
  public static final String ORACLE_AGENT = "oracle";
  
  private static AgentFactory instance;
  private Map<String,Agent> agents;
  
  
  private AgentFactory(){
    agents = new HashMap<String,Agent>();
    this.agents.put(MYSQL_AGENT, new AgentMySQL());
    this.agents.put(ORACLE_AGENT, new AgentOracle());
  }
  
  public static AgentFactory getInstance(){
    return instance == null ? instance = new AgentFactory() : instance;
  }
  
  public Agent getAgent(String agent){
    return this.agents.get(agent);
  }
}
