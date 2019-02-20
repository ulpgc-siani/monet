package org.monet.docservice.docprocessor.data.impl;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.monet.docservice.docprocessor.configuration.Configuration;
import org.monet.docservice.docprocessor.data.DataSourceProvider;

import com.google.inject.Inject;

public class DataSourceProviderImpl implements DataSourceProvider {

  private Configuration configuration;
  private Context context;
  
  @Inject
  public void injectConfiguration(Configuration configuration) {
    this.configuration = configuration;
  }
  
  @Inject
  public void injectContext(Context context) {
    this.context = context;
  }
  
  @Override
  public DataSource get() throws NamingException {
    return (DataSource)this.context.lookup(configuration.getJDBCDataSource());
  }

}
