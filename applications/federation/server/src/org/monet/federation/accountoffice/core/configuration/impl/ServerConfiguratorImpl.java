package org.monet.federation.accountoffice.core.configuration.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.log4j.PropertyConfigurator;
import org.monet.exceptions.ConfigurationException;
import org.monet.federation.accountoffice.core.configuration.ServerConfigurator;
import org.monet.filesystem.StreamHelper;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

@Singleton
public class ServerConfiguratorImpl implements ServerConfigurator {

  private String sUserPath;
  private String sAppDataPath;
  private String sResourcePath;
  private String sImagesPath;

  @Inject
  public ServerConfiguratorImpl(ServletContext context) {
    this.sUserPath = System.getProperty("user.home") + File.separator + "." + context.getServletContextName();
    this.sResourcePath = context.getRealPath("/accountoffice");
    this.sImagesPath = context.getRealPath("/images");
    File fUserFolder = new File(this.sUserPath);
    if (!fUserFolder.exists()) {
      throw new ConfigurationException(String.format("Fatal error: No configuration directory found (%s)", this.sUserPath));
    }

    FileInputStream configurationFileStream = null;
    try {
      Properties properties = new Properties();
      String configurationPath = this.sUserPath + File.separator + "log4j.federation.config";
      configurationFileStream = new FileInputStream(configurationPath);
      properties.loadFromXML(configurationFileStream);
      PropertyConfigurator.configure(properties);
    } catch (Exception e) {
      throw new ConfigurationException("Error loading log4j configuration file", e);
    } finally {
      StreamHelper.close(configurationFileStream);
    }

  }

  @Override
  public String getUserPath() {
    return this.sUserPath;
  }

  @Override
  public String getResourcePath() {
    return this.sResourcePath;
  }

  @Override
  public String getImagesPath() {
    return this.sImagesPath;
  }
  
}
