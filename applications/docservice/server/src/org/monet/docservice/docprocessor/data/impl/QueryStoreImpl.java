package org.monet.docservice.docprocessor.data.impl;

import java.io.InputStream;
import java.util.Properties;

import org.monet.docservice.core.exceptions.ApplicationException;
import org.monet.docservice.core.log.Logger;
import org.monet.docservice.core.util.Resources;
import org.monet.docservice.core.util.StreamHelper;
import org.monet.docservice.docprocessor.configuration.Configuration;
import org.monet.docservice.docprocessor.configuration.ServerConfigurator;
import org.monet.docservice.docprocessor.data.QueryStore;

import com.google.inject.Inject;

public class QueryStoreImpl implements QueryStore {
  private Logger logger;
  private Properties properties;
  
  @Inject
  public QueryStoreImpl(Logger logger, ServerConfigurator serverConfigurator, Configuration configuration) {
    InputStream queryStoreStream = null;
    this.logger = logger;
    this.properties = new Properties();
    try {
      StringBuilder sb = new StringBuilder();
      sb.append("/database/");
      sb.append(configuration.getString(Configuration.JDBC_DATABASE));
      sb.append(".queries.sql");
      String queryStorePath = sb.toString();
      
      queryStoreStream = Resources.getAsStream(queryStorePath); 
      this.properties.load(queryStoreStream);
    } catch (Exception e) {
      this.logger.error("Error loading configuration", e);
    } finally {
      StreamHelper.close(queryStoreStream);
    }
  }

  public String get(String key) {    
    String value = this.properties.getProperty(key);
    if(value != null)
      return value;
    else
      throw new ApplicationException(String.format("Database store error, key '%s' not found", key));
  }
}
