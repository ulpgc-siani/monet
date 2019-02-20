package org.monet.federation.accountoffice.core.database;

import java.util.Properties;

import javax.sql.DataSource;

public interface DataRepositorySource {

  public DataSource getDataSource();
  public Properties getQueries();
  
}
