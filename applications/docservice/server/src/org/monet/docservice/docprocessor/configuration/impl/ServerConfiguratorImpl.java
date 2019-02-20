package org.monet.docservice.docprocessor.configuration.impl;

import com.google.inject.Inject;
import org.apache.log4j.PropertyConfigurator;
import org.monet.docservice.core.exceptions.ApplicationException;
import org.monet.docservice.core.util.StreamHelper;
import org.monet.docservice.docprocessor.configuration.ServerConfigurator;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class ServerConfiguratorImpl implements ServerConfigurator {

  private String sUserPath;
  private String sAppDataPath;
  
  @Inject
  public ServerConfiguratorImpl(ServletContext context) {
    this.sUserPath = System.getProperty("user.home") + File.separator + "." + context.getServletContextName();
    this.sAppDataPath = context.getRealPath("/WEB-INF/app_data");
    File fUserFolder = new File(sUserPath);
    if(!fUserFolder.exists()) {
      throw new ApplicationException(String.format("Fatal error: No configuration directory found (%s)", sUserPath));
    }
    
    FileInputStream configurationFileStream = null;
    try {
      Properties properties = new Properties();
      String configurationPath = this.sUserPath + File.separator + "log4j.docservice.config";
      configurationFileStream = new FileInputStream(configurationPath);
      properties.loadFromXML(configurationFileStream);
      PropertyConfigurator.configure(properties);
    } catch (Exception e) {
      throw new ApplicationException("Error loading log4j configuration file", e);
    } finally {
      StreamHelper.close(configurationFileStream);
    }
    
  }

  public String getUserPath() {
    return this.sUserPath;
  }
  
  public String getAppDataPath() {
    return this.sAppDataPath;
  }
  
}
