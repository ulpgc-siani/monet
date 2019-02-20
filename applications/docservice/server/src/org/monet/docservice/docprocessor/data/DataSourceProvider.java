package org.monet.docservice.docprocessor.data;

import javax.naming.NamingException;
import javax.sql.DataSource;

public interface DataSourceProvider {

  public DataSource get() throws NamingException;
  
}
